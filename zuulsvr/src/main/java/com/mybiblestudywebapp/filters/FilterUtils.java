package com.mybiblestudywebapp.filters;

import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import static com.mybiblestudywebapp.utils.Constants.*;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/19/20
 */
@Component
public class FilterUtils {



    public String getCorrelationId(){
        RequestContext ctx = RequestContext.getCurrentContext();

        if (ctx.getRequest().getHeader(CORRELATION_ID.toString()) !=null) {
            return ctx.getRequest().getHeader(CORRELATION_ID.toString());
        }
        else{
            return  ctx.getZuulRequestHeaders().get(CORRELATION_ID.toString());
        }
    }

    public void setCorrelationId(String correlationId){
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader(CORRELATION_ID.toString(), correlationId);
    }

    public final String getUserId(){
        RequestContext ctx = RequestContext.getCurrentContext();
        if (ctx.getRequest().getHeader(USER_ID.toString()) !=null) {
            return ctx.getRequest().getHeader(USER_ID.toString());
        }
        else{
            return  ctx.getZuulRequestHeaders().get(USER_ID);
        }
    }

    public void setUserId(String userId){
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader(USER_ID.toString(), userId);
    }

    public final String getAuthToken(){
        RequestContext ctx = RequestContext.getCurrentContext();
        return ctx.getRequest().getHeader(AUTH_TOKEN.toString());
    }

    public String getServiceId(){
        RequestContext ctx = RequestContext.getCurrentContext();

        //We might not have a service id if we are using a static, non-eureka route.
        if (ctx.get("serviceId")==null) return "";
        return ctx.get("serviceId").toString();
    }


}
