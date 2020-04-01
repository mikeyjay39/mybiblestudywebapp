package com.mybiblestudywebapp.persistence.request;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 4/1/20
 */
@Data
@Accessors(chain = true)
public class GetStudyNotesForChapterRequest {
    private final String viewCode;
    private final String book;
    private final int chapterNo;
}
