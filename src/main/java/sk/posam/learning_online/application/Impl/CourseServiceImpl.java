package sk.posam.learning_online.application.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.posam.learning_online.application.repository.*;
import sk.posam.learning_online.domain.*;
import sk.posam.learning_online.domain.enumeration.LanguageName;
import sk.posam.learning_online.domain.services.CourseService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
@Autowired
LanguageCrudRepository languageCrudRepository;

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
    public Course updateCourse(Course course, String title, String subtitle, String description,String language,Long categoryId, String imgUrl)  {
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
        if(categoryId != null) {
            Category category = categoryCrudRepository.findById(categoryId).orElse(null);
            if(category != null) {
                Set<Category> courseCategories = course.getCategories();
                if(!courseCategories.isEmpty()){
                    Category courseCategory = courseCategories.iterator().next();
                    course.removeCategory(courseCategory);
                }
                course.addCategory(category);
            }
        }
        if(language != null) {
            Set<Language> courseLanguages = course.getLanguages();
            if(!courseLanguages.isEmpty()) {
                Language courseLanguage = courseLanguages.iterator().next();
                course.removeLanguage(courseLanguage);
            }
            LanguageName languageName = LanguageName.valueOf(language);
            Language foundLanguage = languageCrudRepository.findByName(languageName).orElse(null);


            if(foundLanguage != null) {
                course.addLanguage(foundLanguage);
            }
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
        return courseCrudRepository.save(course);
        }
    }

    @Override
    public Section updateLecture(Section section, Long lectureId, String title, Integer durationInSeconds, Integer rank, String sourceUrl) {
        Lecture lecture = lectureCrudRepository.findById(lectureId).orElse(null);
        if(section == null) return null;
        if(lecture !=null) {
            if(!lecture.getSection().equals(section)) {
                return null;
            }
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
            return null;
        }
        return sectionCrudRepository.save(section);

    }
    @Override
    public Section createLecture(Section section, String title, Integer durationInSeconds, Integer rank, String sourceUrl) {
        if(section == null) return null;
        if(title == null || durationInSeconds == null || rank == null || sourceUrl == null) return null;
        Lecture lecture = new Lecture(title,durationInSeconds,sourceUrl,rank);
        section.addVideo(lecture);
        return sectionCrudRepository.save(section);
    }

    @Override
    public Section removeLecture(Section section, Long lectureId) {
        if(section == null || lectureId == null) return null;
        Lecture lecture = lectureCrudRepository.findById(lectureId).orElse(null);
            if(lecture != null) {
            section.removeLecture(lecture);
            return sectionCrudRepository.save(section);
            } else {
                return null;
            }
    }

    @Override
    public Course publishCourse(Course course) {
        if(course == null) {
            return null;
        }

        if(
                course.getTitle().length() >= 15
                && course.getSubtitle().length() >=15
                && course.getDescription().length() >=15
                && !course.getCategories().isEmpty()
                && !course.getLanguages().isEmpty()
                && course.getImageUrl().length() > 0
                && course.getWhatYouWillLearn().size() >=4
                && course.getPrice() != null
                && course.getSections().size() >=1
                && !course.getSections().iterator().next().getVideos().isEmpty()
        ) {
            course.setDraft(false);
            course.setLastUpdated(LocalDateTime.now());
            return courseCrudRepository.save(course);
        }
        return null;
    }


    public List<Course> getAllCoursesForTeacher(Long userId) {
        return courseCrudRepository.findAllByUserId(userId);
    }

    public Page<Course> searchCoursesByTitle(String searchTerm, Pageable pageable) {
        return courseCrudRepository.findByTitleIgnoreCaseContainingAndDraftIsFalse(searchTerm, pageable);
    }


}
