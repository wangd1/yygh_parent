package com.syt.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.syt.yygh.common.exception.YyghException;
import com.syt.yygh.common.result.ResultCodeEnum;
import com.syt.yygh.hosp.repository.ScheduleRepository;
import com.syt.yygh.hosp.service.DepartmentService;
import com.syt.yygh.hosp.service.HospitalService;
import com.syt.yygh.hosp.service.ScheduleService;
import com.syt.yygh.model.hosp.BookingRule;
import com.syt.yygh.model.hosp.Department;
import com.syt.yygh.model.hosp.Hospital;
import com.syt.yygh.model.hosp.Schedule;
import com.syt.yygh.vo.hosp.BookingScheduleRuleVo;
import com.syt.yygh.vo.hosp.ScheduleOrderVo;
import com.syt.yygh.vo.hosp.ScheduleQueryVo;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.security.cert.Certificate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: wangdi
 * @Date: 2021/3/14
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private DepartmentService departmentService;

    @Override
    public void save(Map<String, Object> paramMap) {
        Schedule schedule = JSONObject.parseObject(JSONObject.toJSONString(paramMap), Schedule.class);
        Schedule scheduleExists = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(), schedule.getHosScheduleId());
        if(scheduleExists==null){
            // ??????
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            schedule.setStatus(1);
            scheduleRepository.save(schedule);
        }else{
            // ??????
            scheduleExists.setUpdateTime(new Date());
            scheduleExists.setIsDeleted(0);
            scheduleExists.setStatus(1);
            scheduleRepository.save(scheduleExists);
        }
    }

    @Override
    public Page<Schedule> findPage(int page, int limit, ScheduleQueryVo scheduleQueryVo) {
        Pageable pageable = PageRequest.of(page-1,limit);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo,schedule);
        schedule.setIsDeleted(0);
        schedule.setStatus(1);
        Example<Schedule> example = Example.of(schedule,matcher);

        return scheduleRepository.findAll(example,pageable);
    }

    @Override
    public void removeSchedule(String code, String hosScheduleId) {
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(code, hosScheduleId);
        if(schedule!=null){
            scheduleRepository.deleteById(schedule.getId());
        }
    }

    @Override
    public Map<String, Object> getScheduleRules(long page, long limit, String hoscode, String depcode) {
        // ???????????????????????????????????????
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);
        // ???????????????workDate??????
        Aggregation aggregation = Aggregation.newAggregation(
                //????????????
                Aggregation.match(criteria),
                //????????????
                Aggregation.group("workDate").first("workDate").as("workDate")
                        // ??????????????????
                        .count().as("docCount")
                        .sum("reservedNumber").as("reservedNumber")
                        .sum("availableNumber").as("availableNumber"),
                Aggregation.sort(Sort.Direction.DESC,"workDate"),
                Aggregation.skip((page-1)*limit),
                Aggregation.limit(limit)
        );
        AggregationResults<BookingScheduleRuleVo> aggResult =
                mongoTemplate.aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> bookVoList = aggResult.getMappedResults();

        // ??????????????????
        Aggregation totalAgg = Aggregation.newAggregation(
                //????????????
                Aggregation.match(criteria),
                //????????????
                Aggregation.group("workDate")
        );
        AggregationResults<BookingScheduleRuleVo> totalResult =
                mongoTemplate.aggregate(totalAgg, Schedule.class, BookingScheduleRuleVo.class);
        int total = totalResult.getMappedResults().size();
        // ???????????????
        for (BookingScheduleRuleVo vo : bookVoList) {
            Date workDate = vo.getWorkDate();
            String dayOfWeek = getDayOfWeek(new DateTime(workDate));
            vo.setDayOfWeek(dayOfWeek);
        }
        Map<String,Object> map = new HashMap<>(3);
        map.put("bookVoList",bookVoList);
        map.put("total",total);
        // ??????????????????
        String hospName = hospitalService.getHospitalByHosCode(hoscode).getHosname();
        map.put("hospName",hospName);
        return map;
    }

    @Override
    public List<Schedule> getScheduleDetail(String hoscode, String depcode, String workDate) {
        Date workDate1 = new DateTime(workDate).toDate();
        List<Schedule> schedules = scheduleRepository.getScheduleByHoscodeAndDepcodeAndWorkDate(hoscode, depcode, workDate1);
        // ??????????????????????????????????????????????????????
        schedules.forEach(this::packageSchedule);
        return schedules;
    }

    @Override
    public Map<String, Object> getBookingScheduleRule(Integer page, Integer limit, String hoscode, String depcode) {
        Map<String, Object> result = new HashMap<>(6);
        // ??????????????????
        Hospital hospital = hospitalService.getHospitalByHosCode(hoscode);
        if(hospital==null){
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        BookingRule bookingRule = hospital.getBookingRule();
        // ???????????????????????????????????????
        IPage<Date> iPage = getDateList(page, limit, bookingRule);
        List<Date> dateList = iPage.getRecords();
        // ?????????????????????????????????
        Criteria criteria = Criteria.where("hoscode").is(hoscode)
                .and("depcode").is(depcode).and("workDate").in(dateList);
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate").first("workDate").as("workDate")
                .count().as("docCount")
                        .sum("availableNumber").as("availableNumber")
                        .sum("reservedNumber").as("reservedNumber")

        );
        AggregationResults<BookingScheduleRuleVo> aggregate =
                mongoTemplate.aggregate(agg, Schedule.class, BookingScheduleRuleVo.class);
        // ????????????
        List<BookingScheduleRuleVo> scheduleRuleVos = aggregate.getMappedResults();
        // ???????????????map()
        Map<Date, BookingScheduleRuleVo> scheduleVoMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(scheduleRuleVos)) {
            scheduleVoMap = scheduleRuleVos.stream().collect(Collectors.toMap(BookingScheduleRuleVo::getWorkDate, bookingScheduleRuleVo -> bookingScheduleRuleVo));
        }
        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = new ArrayList<>();
        // ??????????????????????????????
        for (int i = 0,len = dateList.size(); i < len; i++) {
            Date key = dateList.get(i);
            BookingScheduleRuleVo bookingScheduleRuleVo = scheduleVoMap.get(key);
            if(bookingScheduleRuleVo==null){
                // ??????????????????
                bookingScheduleRuleVo = new BookingScheduleRuleVo();
                //??????????????????
                bookingScheduleRuleVo.setDocCount(0);
                //?????????????????????  -1????????????
                bookingScheduleRuleVo.setAvailableNumber(-1);
            }
            bookingScheduleRuleVo.setWorkDate(key);
            bookingScheduleRuleVo.setWorkDateMd(key);
            // ?????????????????????
            bookingScheduleRuleVo.setDayOfWeek(this.getDayOfWeek(new DateTime(key)));

            //?????????????????????????????????????????????   ?????? 0????????? 1??????????????? -1????????????????????????
            if(i == len-1 && page == iPage.getPages()) {
                bookingScheduleRuleVo.setStatus(1);
            } else {
                bookingScheduleRuleVo.setStatus(0);
            }
            //??????????????????????????????????????? ????????????
            if(i == 0 && page == 1) {
                DateTime stopTime = this.getDateTime(new Date(), bookingRule.getStopTime());
                if(stopTime.isBeforeNow()) {
                    //????????????
                    bookingScheduleRuleVo.setStatus(-1);
                }
            }
            bookingScheduleRuleVoList.add(bookingScheduleRuleVo);
        }

        //???????????????????????????
        result.put("bookingScheduleList", bookingScheduleRuleVoList);
        result.put("total", iPage.getTotal());
        //??????????????????
        Map<String, String> baseMap = new HashMap<>(6);
        //????????????
        baseMap.put("hosname", hospitalService.getHospitalByHosCode(hoscode).getHosname());
        //??????
        Department department =departmentService.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        //???????????????
        baseMap.put("bigname", department.getBigname());
        //????????????
        baseMap.put("depname", department.getDepname());
        //???
        baseMap.put("workDateString", new DateTime().toString("yyyy???MM???"));
        //????????????
        baseMap.put("releaseTime", bookingRule.getReleaseTime());
        //????????????
        baseMap.put("stopTime", bookingRule.getStopTime());
        result.put("baseMap", baseMap);
        return result;
    }

    @Override
    public Schedule getById(String id) {
        Schedule schedule = scheduleRepository.findById(id).get();
        this.packageSchedule(schedule);
        return schedule;
    }

    @Override
    public ScheduleOrderVo getScheduleOrderVo(String scheduleId) {
        ScheduleOrderVo scheduleOrderVo = new ScheduleOrderVo();
        //????????????
        Schedule schedule = scheduleRepository.getScheduleById(scheduleId);
        if(null == schedule) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        //????????????????????????
        Hospital hospital = hospitalService.getHospitalByHosCode(schedule.getHoscode());
        if(null == hospital) {
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        BookingRule bookingRule = hospital.getBookingRule();
        if(null == bookingRule) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        scheduleOrderVo.setHoscode(schedule.getHoscode());
        scheduleOrderVo.setHosname(hospitalService.getHospitalByHosCode(schedule.getHoscode()).getHosname());
        scheduleOrderVo.setDepcode(schedule.getDepcode());
        scheduleOrderVo.setDepname(departmentService.getDepartmentByHoscodeAndDepcode(schedule.getHoscode(), schedule.getDepcode()).getDepname());
        scheduleOrderVo.setHosScheduleId(schedule.getHosScheduleId());
        scheduleOrderVo.setAvailableNumber(schedule.getAvailableNumber());
        scheduleOrderVo.setTitle(schedule.getTitle());
        scheduleOrderVo.setReserveDate(schedule.getWorkDate());
        scheduleOrderVo.setReserveTime(schedule.getWorkTime());
        scheduleOrderVo.setAmount(schedule.getAmount());

        //?????????????????????????????????????????????-1????????????0???
        int quitDay = bookingRule.getQuitDay();
        DateTime quitTime = this.getDateTime(new DateTime(schedule.getWorkDate()).plusDays(quitDay).toDate(), bookingRule.getQuitTime());
        scheduleOrderVo.setQuitTime(quitTime.toDate());

        //??????????????????
        DateTime startTime = this.getDateTime(new Date(), bookingRule.getReleaseTime());
        scheduleOrderVo.setStartTime(startTime.toDate());

        //??????????????????
        DateTime endTime = this.getDateTime(new DateTime().plusDays(bookingRule.getCycle()).toDate(), bookingRule.getStopTime());
        scheduleOrderVo.setEndTime(endTime.toDate());

        //????????????????????????
        DateTime stopTime = this.getDateTime(new Date(), bookingRule.getStopTime());
        scheduleOrderVo.setStopTime(stopTime.toDate());
        return scheduleOrderVo;
    }

    @Override
    public void update(Schedule schedule) {
        schedule.setUpdateTime(new Date());
        //????????????????????????
        scheduleRepository.save(schedule);
    }

    private IPage<Date> getDateList(int page,int limit,BookingRule bookingRule){
        // ????????????????????????
        DateTime releaseTime = getDateTime(new Date(), bookingRule.getReleaseTime());
        Integer cycle = bookingRule.getCycle();
        // ????????????????????????????????????????????????????????????
        if(releaseTime.isBeforeNow()){
            cycle+=1;
        }
        // ???????????????????????????????????????????????????????????????
        List<Date> dateList = new ArrayList<>();
        for (int i = 0; i < cycle; i++) {
            DateTime dateTime = releaseTime.plusDays(i);
            Date date = new DateTime(dateTime.toString("yyyy-MM-dd")).toDate();
            dateList.add(date);
        }
        // ?????????????????????????????????????????????????????????
        List<Date> pageDateList = new ArrayList<>();
        int start = (page-1)*limit;
        int end = start+limit;
        if(end>dateList.size()){
            end = dateList.size();
        }
        for (int i = start; i < end; i++) {
            pageDateList.add(dateList.get(i));
        }
        // ??????
        IPage<Date> iPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page,7,dateList.size());
        iPage.setRecords(pageDateList);
        return iPage;
    }

    /**
     * ???Date?????????yyyy-MM-dd HH:mm????????????DateTime
     */
    private DateTime getDateTime(Date date, String timeString) {
        String dateTimeString = new DateTime(date).toString("yyyy-MM-dd") + " "+ timeString;
        return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(dateTimeString);
    }


    /**
     * ??????????????????
     * @param schedule ??????
     */
    private void packageSchedule(Schedule schedule) {
        schedule.getParam().put("hosname",hospitalService.getHospitalByHosCode(schedule.getHoscode()).getHosname());
        schedule.getParam().put("depname",departmentService.getDepartmentByHoscodeAndDepcode(schedule.getHoscode(),schedule.getDepcode()).getDepname());
        schedule.getParam().put("dayOfWeek",getDayOfWeek(new DateTime(schedule.getWorkDate())));

    }

    /**
     * ??????????????????????????????
     * @param dateTime ??????
     * @return ?????????
     */
    private String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "??????";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "??????";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "??????";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "??????";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "??????";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "??????";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "??????";
            default:
                break;
        }
        return dayOfWeek;
    }
}
