package zerobase.sijak.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.sijak.dto.*;
import zerobase.sijak.persist.domain.Lecture;
import zerobase.sijak.service.LectureService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LectureController {

    private final LectureService lectureService;

    @GetMapping("/home")
    public ResponseEntity<HttpResponse> readHome(@RequestHeader("Authorization") String token,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "4") int size,
                                                 @RequestBody PositionInfo positionInfo) {

        double latitude = positionInfo.getLatitude();
        double longitude = positionInfo.getLongitude();
        Pageable pageable = PageRequest.of(page, size);
        Slice<LectureHomeResponse> lectures = lectureService.readHome(token, pageable, latitude, longitude);
        List<PickHomeResponse> pickClasses = lectureService.getPickClasses(token);

        Map<String, Object> totalList = new HashMap<>();
        totalList.put("data", lectures.getContent());
        totalList.put("hasNext", lectures.hasNext());
        totalList.put("pickClasses", pickClasses);

        return ResponseEntity.ok(HttpResponse.res(HttpStatus.OK.value(), HttpStatus.OK.toString(), totalList));
    }

    @GetMapping("/lectures")
    public ResponseEntity<HttpResponse> readLectures(@RequestHeader("Authorization") String token,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "9") int size,
                                                     @RequestBody PositionInfo positionInfo) {

        double latitude = positionInfo.getLatitude();
        double longitude = positionInfo.getLongitude();
        Pageable pageable = PageRequest.of(page, size);
        Slice<LectureHomeResponse> lectures = lectureService.readLectures(token, pageable, longitude, latitude);

        Map<String, Object> totalList = new HashMap<>();
        totalList.put("data", lectures.getContent());
        totalList.put("hasNext", lectures.hasNext());

        return ResponseEntity.ok(HttpResponse.res(HttpStatus.OK.value(), HttpStatus.OK.toString(), totalList));
    }


    @GetMapping("/lectures/{id}")
    public ResponseEntity<HttpResponse> readLecture(@RequestHeader("Authorization") String token, @PathVariable int id,
                                                    @RequestBody PositionInfo positionInfo) {

        double latitude = positionInfo.getLatitude();
        double longitude = positionInfo.getLongitude();
        LectureDetailResponse lectureDetailResponse = lectureService.readLecture(token, id, latitude, longitude);

        return ResponseEntity.ok(HttpResponse.res(HttpStatus.OK.value(), HttpStatus.OK.toString(), lectureDetailResponse));
    }


}
