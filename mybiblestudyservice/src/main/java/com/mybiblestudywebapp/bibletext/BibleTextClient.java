package com.mybiblestudywebapp.bibletext;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/16/20
 */
@FeignClient("bibletextservice")
public interface BibleTextClient {
}
