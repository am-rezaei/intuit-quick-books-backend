package com.intuit.developer.helloworld.controller;

import com.intuit.developer.helloworld.service.BillService;
import com.intuit.ipp.exception.FMSException;
import com.intuit.ipp.services.QueryResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class BillController {

    final BillService billService;

    @ResponseBody
    @RequestMapping("/getBills")
    public QueryResult getBills(@RequestParam("type") String type, @RequestParam("referenceTime") Long referenceTimeEpoch, HttpServletRequest request) throws FMSException {
        String accessToken = request.getHeader("access_token");
        String realmId = request.getHeader("realm_id");
        return billService.getBills(accessToken, realmId, type, referenceTimeEpoch);
    }

}
