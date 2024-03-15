package org.xavier.star.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xavier.star.entity.CurrentEvent;
import org.xavier.star.entity.CurrentEventImg;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("近期活动和对应图片列表的组合")
public class CurrentEventWithImg {

    /**
     * {@link CurrentEvent}
     */
    private CurrentEvent currentEvent;

    private List<CurrentEventImg> currentEventImgList;
}
