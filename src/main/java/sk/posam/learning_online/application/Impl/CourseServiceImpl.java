package sk.posam.learning_online.application.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.posam.learning_online.application.*;
import sk.posam.learning_online.domain.*;
import sk.posam.learning_online.domain.services.CourseService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {
    @Autowired
    CourseCrudRepository courseCrudRepository;

    @Autowired
    CategoryCrudRepository categoryCrudRepository;

    @Autowired
    UserCrudRepository userCrudRepository;
    @Autowired
    WhatYouWillLearnCrudRepository whatYouWillLearnCrudRepository;
    @Autowired
    SectionCrudRepository sectionCrudRepository;

    @Autowired
    LectureCrudRepository lectureCrudRepository;

    @Override
    public Collection<Course> getAllCoursesForCategory(Long categoryId) {
        return courseCrudRepository.findByCategoryId(categoryId);
    }

    @Override
    public Course createCourseOnClient(String title, Long categoryId,Long userId) throws NotFoundException {
        Course newCourse = new Course(title);
        Category category = categoryCrudRepository.findById(categoryId).orElseThrow(NotFoundException::new);
        User user = userCrudRepository.findById(userId).orElseThrow(NotFoundException::new);
        newCourse.setUser(user);
        newCourse.addCategory(category);
        newCourse.setDraft(true);
        newCourse.setCreatedAt(LocalDateTime.now());
        return courseCrudRepository.save(newCourse);
    }

    @Override
    public Course getCourseForTeacher(Long userId, Long courseId) throws NotFoundException {
      Course course =  this.courseCrudRepository.findCourseByTeacherIdAndCourseId(userId,courseId).orElseThrow(NotFoundException::new);
      return course;
    }

    @Override
    public Course updateCourse(Course course, String title, String subtitle, String description, String imgUrl)  {
        if(course == null) {
            return null;
        }
        if(title != null && title.length() >= 15) {
            course.setTitle(title);
        }
        if(subtitle != null && !subtitle.equals("")) {
            course.setSubtitle(subtitle);
        }
        if(description != null && !description.equals("")) {
            course.setDescription(description);
        }
        if(imgUrl != null) {
            course.setImageUrl(imgUrl);
        }

       return courseCrudRepository.save(course);
    }

    @Override
    public Course updateCoursePrice(Course course, Double price) {
        if(course == null || price == null) {
            return null;
        } else {
            course.setPrice(price);
        }
        return courseCrudRepository.save(course);
    }

    @Override
    public Course updateCourseWhatYouWillLearn(Course course, ArrayList<String> sentences) {
        if(course == null || sentences == null) {
            return null;
        } else {
            List<WhatYouWillLearn> oldSentences = whatYouWillLearnCrudRepository.findByCourseId(course.getId());
            Iterable<WhatYouWillLearn> iterable = new ArrayList<>(oldSentences);
            whatYouWillLearnCrudRepository.deleteAll(iterable);
            for (String sentence: sentences
                 ) {
                if(sentence.equals("")) {
                    continue;
                }
                WhatYouWillLearn whatYouWillLearn = new WhatYouWillLearn(sentence);
                course.addWYWL(whatYouWillLearn);
            }

        }
        return courseCrudRepository.save(course);
    }

    @Override
    public Course addOrUpdateSection(Course course, String title, Long sectionId,Integer rank) {
        if(course == null ) {
            return null;
        }
        Section section = null;
        if((sectionId != null)) {
//            Looking for section
            section = sectionCrudRepository.findById(sectionId).orElse(null);
        }
        if (
           section == null
        ) {
//            Section is not found thus creating new one
            Section newSection = new Section(title);
            if(rank != null) {
                newSection.setRank(rank);
            }
            course.addSection(newSection);
        } else {
            //            Section is found thus updating it
            section.setTitle(title);
            if (rank != null) {
            section.setRank(rank);
            }
        }
        return courseCrudRepository.save(course);
    }

    @Override
    public Course removeSection(Course course, Long sectionId) {
        Section section = sectionCrudRepository.findById(sectionId).orElse(null);
        if(course == null || section == null) {
            return null;
        }else {
            course.removeSection(section);
//            sectionCrudRepository.delete(section);
        }
        return courseCrudRepository.save(course);
    }

    @Override
    public Section addOrUpdateLecture(Long sectionId, Long lectureId, String title, Integer durationInSeconds, Integer rank, String sourceUrl) {
        Section section = sectionCrudRepository.findById(sectionId).orElse(null);
        if(section == null) return null;
        Lecture lecture = lectureCrudRepository.findById(lectureId).orElse(null);
        if(lecture != null) {
            if(title != null) {
                lecture.setTitle(title);
            }
            if(durationInSeconds != null) {
                lecture.setDurationInSeconds(durationInSeconds);
            }
            if(rank != null) {
                lecture.setRank(rank);
            }
            if(sourceUrl != null) {
                lecture.setSourceUrl(sourceUrl);
            }
        } else {
            if(title == null || durationInSeconds == null || rank == null || sourceUrl == null) return null;
            lecture = new Lecture(title,durationInSeconds,sourceUrl,rank);
            section.addVideo(lecture);
        }
        return sectionCrudRepository.save(section);

    }


    public List<Course> getAllCoursesForTeacher(Long userId) {
        return courseCrudRepository.findAllByUserId(userId);
    }

    public Page<Course> searchCoursesByTitle(String searchTerm, Pageable pageable) {
        return courseCrudRepository.findByTitleIgnoreCaseContainingAndDraftIsFalse(searchTerm, pageable);
    }


}
