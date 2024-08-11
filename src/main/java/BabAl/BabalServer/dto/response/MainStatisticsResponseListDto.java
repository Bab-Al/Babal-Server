package BabAl.BabalServer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MainStatisticsResponseListDto {
    private List<MainStatisticsResponseDto> data;

    public static MainStatisticsResponseListDto mainStatisticsResponseList(List<MainStatisticsResponseDto> dtoList, List<LocalDate> allDates) {
        // 날짜별로 데이터를 맵핑
        Map<LocalDate, MainStatisticsResponseDto> dtoMap = dtoList.stream()
                .collect(Collectors.toMap(MainStatisticsResponseDto::getDate, dto -> dto));

        // 모든 날짜에 대해 데이터가 없으면 기본값 생성
        List<MainStatisticsResponseDto> fullDtoList = allDates.stream()
                .map(date -> dtoMap.getOrDefault(date, MainStatisticsResponseDto.createWithDefaults(date)))
                .toList();

        return MainStatisticsResponseListDto.builder()
                .data(fullDtoList)
                .build();
    }
}