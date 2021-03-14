package com.syt.yygh.hosp.controller.api;

import com.syt.yygh.common.exception.YyghException;
import com.syt.yygh.common.helper.HttpRequestHelper;
import com.syt.yygh.common.result.Result;
import com.syt.yygh.common.result.ResultCodeEnum;
import com.syt.yygh.common.util.MD5;
import com.syt.yygh.hosp.service.DepartmentService;
import com.syt.yygh.hosp.service.HospitalService;
import com.syt.yygh.hosp.service.HospitalSetService;
import com.syt.yygh.hosp.service.ScheduleService;
import com.syt.yygh.model.hosp.Department;
import com.syt.yygh.model.hosp.Schedule;
import com.syt.yygh.vo.hosp.DepartmentQueryVo;
import com.syt.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @Author: wangdi
 * @Date: 2021/3/13
 */
@RestController
@RequestMapping("/api/hosp")
public class ApiController {

    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private HospitalSetService hospitalSetService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private ScheduleService scheduleService;

    /**
     * 删除排版
     * @param request
     * @return
     */
    @PostMapping("/schedule/remove")
    public Result removeSchedule(HttpServletRequest request){
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String sign = (String) paramMap.get("sign");
        String code = (String) paramMap.get("hoscode");
        String hosScheduleId = (String) paramMap.get("hosScheduleId");
        if(StringUtils.isEmpty(code)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        // 签名校验
        signCheck(sign, code);
        scheduleService.removeSchedule(code,hosScheduleId);
        return Result.ok();
    }

    /**
     * 查询排班列表
     * @param request
     * @return
     */
    @PostMapping("/schedule/list")
    public Result getScheduleList(HttpServletRequest request){
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String sign = (String) paramMap.get("sign");
        String code = (String) paramMap.get("hoscode");
        String depCode = (String) paramMap.get("depcode");
        if(StringUtils.isEmpty(code)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        // 签名校验
        signCheck(sign, code);
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String) paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 10 : Integer.parseInt((String) paramMap.get("limit"));

        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(code);
        scheduleQueryVo.setDepcode(depCode);
        Page<Schedule> list = scheduleService.findPage(page,limit,scheduleQueryVo);
        return Result.ok(list);
    }

    /**
     * 上传排班接口
     *
     * @return
     */
    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String sign = (String) paramMap.get("sign");
        String code = (String) paramMap.get("hoscode");
        if(StringUtils.isEmpty(code)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        signCheck(sign, code);
        scheduleService.save(paramMap);
        return Result.ok();
    }

    /**
     * 删除科室
     * @param request
     * @return
     */
    @PostMapping("/department/remove")
    public Result removeDepartment(HttpServletRequest request){
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String sign = (String) paramMap.get("sign");
        String code = (String) paramMap.get("hoscode");
        if(StringUtils.isEmpty(code)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        // 签名校验
        signCheck(sign, code);
        String depcode = (String) paramMap.get("depcode");
        departmentService.removeDepartment(code,depcode);
        return Result.ok();
    }

    /**
     * 查询科室列表
     * @param request
     * @return
     */
    @PostMapping("/department/list")
    public Result getDepartmentList(HttpServletRequest request){
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String sign = (String) paramMap.get("sign");
        String code = (String) paramMap.get("hoscode");
        if(StringUtils.isEmpty(code)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        // 签名校验
        signCheck(sign, code);
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String) paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 10 : Integer.parseInt((String) paramMap.get("limit"));

        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(code);
        Page<Department> list = departmentService.findPage(page,limit,departmentQueryVo);
        return Result.ok(list);
    }

    /**
     * 上传科室接口
     *
     * @return
     */
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String sign = (String) paramMap.get("sign");
        String code = (String) paramMap.get("hoscode");
        if(StringUtils.isEmpty(code)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        signCheck(sign, code);
        departmentService.save(paramMap);
        return Result.ok();
    }


    /**
     * 查询医院
     * @param request
     * @return
     */
    @PostMapping("/hospital/show")
    public Result getHospital(HttpServletRequest request){
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String sign = (String) paramMap.get("sign");
        String code = (String) paramMap.get("hoscode");
        signCheck(sign,code);
        return Result.ok(hospitalService.getHospitalByHosCode(code));
    }

    /**
     * 上传医院接口
     *
     * @return
     */
    @PostMapping("saveHospital")
    public Result saveHospital(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String sign = (String) paramMap.get("sign");
        String code = (String) paramMap.get("hoscode");
        if(StringUtils.isEmpty(code)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        //传输过程中“+”转换为了“ ”，因此我们要转换回来
        String logoDataString = (String) paramMap.get("logoData");
        if(!StringUtils.isEmpty(logoDataString)) {
            String logoData = logoDataString.replaceAll(" ", "+");
            paramMap.put("logoData", logoData);
        }
        signCheck(sign, code);
        hospitalService.save(paramMap);
        return Result.ok();
    }

    /**
     * 签名检查
     * @param sign
     * @param code
     */
    private void signCheck(String sign, String code) {
        //签名校验
        if(!MD5.encrypt(hospitalSetService.getSignKey(code)).equals(sign)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
    }

}
