package com.ll.traveler.global.initData;

import com.ll.traveler.domain.member.member.entity.Member;
import com.ll.traveler.domain.member.member.service.MemberService;
import com.ll.traveler.domain.post.PostCategory.entity.PostCategory;
import com.ll.traveler.domain.post.post.entity.Post;
import com.ll.traveler.domain.post.post.repository.PostRepository;
import com.ll.traveler.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Profile("!prod")
@Slf4j
@RequiredArgsConstructor
public class NotProd {
    @Autowired
    @Lazy
    private NotProd self;
    private final MemberService memberService;
    private final PostService postService;
    private final PostRepository postRepository;

    @Bean
    @Order(3)
    public ApplicationRunner initNotProd() {
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public  void work1() {
        if(postRepository.count() > 0) return;

        Member user1 = memberService.join("MEMBER","user1", "1234", "user1@test.com","nickname1","").getData();
        Member user2 = memberService.join("MEMBER","user2", "1234", "user2@test.com","nickname2","").getData();
        Member user3 = memberService.join("MEMBER","user3", "1234", "user3@test.com","nickname3","").getData();
        Member user4 = memberService.join("MEMBER","user4", "1234", "user4@test.com","nickname4","").getData();

        for(int i = 1; i <= 10; i++) {
            Post post = postService.write(user1, "제목 " + i, "내용" + i, "광주", "서구");
            PostCategory category = PostCategory.builder().content("숙소").author(user1).post(post).build();
            post.getCategories().add(category);
            postService.like(user2, post);

        }

        for(int i = 1; i <= 10; i++) {
            Post post = postService.write(user2, "제목 " + i, "내용 " + i, "부산", "영도구");
            PostCategory category = PostCategory.builder().content("산책길").author(user2).post(post).build();
            post.getCategories().add(category);
        }

        for(int i = 1; i <= 10; i++) {
            Post post = postService.write(user3, "제목 " + i, "내용 " + i, "대구", "달서구");
            PostCategory category = PostCategory.builder().content("식당").author(user3).post(post).build();
            post.getCategories().add(category);
        }

        for(int i = 1; i <= 10; i++) {
            Post post = postService.write(user4, "제목 " + i, "이 글은 광주에서 유명한 애견 동반 카페를 공유드리기 위해 작성한 글입니다." + i, "서울", "강남구");
            PostCategory category1 = PostCategory.builder().content("식당").author(user3).post(post).build();
            PostCategory category2 = PostCategory.builder().content("카페").author(user3).post(post).build();

            post.getCategories().add(category1);
            post.getCategories().add(category2);

            postService.like(user2, post);
            postService.like(user3, post);
        }



    }
}

