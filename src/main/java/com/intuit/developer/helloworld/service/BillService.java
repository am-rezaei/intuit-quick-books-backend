package com.intuit.developer.helloworld.service;

import com.intuit.developer.helloworld.helper.QBOServiceHelper;
import com.intuit.ipp.exception.FMSException;
import com.intuit.ipp.services.DataService;
import com.intuit.ipp.services.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Service
public class BillService {

    @Autowired
    public QBOServiceHelper helper;

    public QueryResult getBills(String accessToken, String realmId, String type, Long referenceTimeEpoch) throws FMSException {
        DataService dateService = helper.getDataService(realmId, accessToken);

        if (referenceTimeEpoch == 0 &&
                (type.equalsIgnoreCase("modified") || type.equalsIgnoreCase("new")))
            return new QueryResult();

        String sql = "select * from bill";

        String referenceTime = getDateTimeFromEpoch(referenceTimeEpoch);

        if (type.equalsIgnoreCase("modified")) {
            sql = String.format("select * from bill where metadata.lastupdatedtime > '%s' and metadata.createtime < '%s'", referenceTime, referenceTime);
        }

        if (type.equalsIgnoreCase("new")) {
            sql = String.format("select * from bill where metadata.createtime > '%s'", referenceTime);
        }

        return dateService.executeQuery(sql);
    }


    private String getDateTimeFromEpoch(Long epoch) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        return  formatter.format(new Date(epoch));
    }

}
