package kosta.springjspblog.web.controller;

import kosta.springjspblog.domain.dto.*;
import kosta.springjspblog.domain.service.ArticleService;
import kosta.springjspblog.domain.service.BlogService;
import kosta.springjspblog.domain.service.CategoryService;
import kosta.springjspblog.web.error.BadRequestException;
import kosta.springjspblog.web.error.ForbiddenException;
import kosta.springjspblog.web.error.NoContentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/blog")
public class BlogController {

    private final BlogService blogService;
    private final CategoryService categoryService;
    private final ArticleService articleService;

    @GetMapping(value = "/{id}/create")
    public String createForm() {
        return "blog/blog-createForm";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Blog blog, HttpSession session) {
        User user = (User) session.getAttribute("authUser");

        if (blog == null) {
            return "redirect:/result=fail";
        }

        Blog createdBlog = blogService.create(blog, user);
        session.setAttribute("blog", createdBlog);
        return "blog/blog-main";
    }

    @GetMapping(value = "/{id}")
    public String myBlog(@PathVariable("id") String id,
                         @RequestParam(value = "categoryNo", defaultValue = "0") int categoryNo,
                         @RequestParam(value = "articleNo", defaultValue = "0") int articleNo,
                         Model model) {

        Blog blog = blogService.getBlog(id);
        List<Category> categories = categoryService.getCategories(blog.getUserId());
        List<Article> articles = articleService.getArticles(blog.getUserId(), categoryNo);

        if (articleNo == 0 && getArticlesSize(articles) != 0) {
            articleNo = articleAtFirst(articles).getArticleNo();
        }

        Article article = articleService.getArticle(articleNo);

        log.info("blog = {}", blog);
        log.info("article = {}", article);
        log.info("categories = {}", categories);

        model.addAttribute("blog", blog);
        model.addAttribute("categories", categories);
        model.addAttribute("articles", articles);
        model.addAttribute("article", article);
        model.addAttribute("newLine", "\r\n");
        return "blog/blog-main";
    }

    @GetMapping(value = "/{id}/admin/basic")
    public String blogAdminBasic(@PathVariable("id") String id, HttpSession session, Model model) {
        User authUser = (User) session.getAttribute("authUser");

        if (isLoginAndProperUser(id, authUser)) {
            Blog blog = blogService.getBlog(id);
            model.addAttribute("blog", blog);
            return "blog/admin/blog-admin-basic";
        }
        throw new ForbiddenException();
    }

    @PostMapping(value = "/{id}/admin/basicModify")
    public String blogAdminBasicModify(@ModelAttribute Blog blog, HttpSession session, Model model) throws IOException {
        User authUser = (User) session.getAttribute("authUser");

        if (isLoginAndProperUser(authUser.getId(), authUser)) {
            blogService.blogAdminBasicModify(blog, authUser);
            return String.format("redirect:/blog/%s/admin/basic", blog.getUserId());
        }
        throw new BadRequestException();
    }

    @GetMapping(value = "/{id}/admin/category")
    public String blogAdminCate(@PathVariable("id") String id, HttpSession session, Model model) {
        User authUser = (User) session.getAttribute("authUser");

        if (isLoginAndProperUser(id, authUser)) {
            Blog blog = blogService.getBlog(id);
            List<Category> categories = categoryService.getCategories(blog.getUserId());

            model.addAttribute("blog", blog);
            model.addAttribute("categories", categories);
            return "/blog/admin/blog-admin-category";
        }
        throw new BadRequestException();
    }

    @GetMapping(value = "/{id}/admin/write")
    public String blogAdminWrite(@PathVariable("id") String id, HttpSession session, Model model) {
        User authUser = (User) session.getAttribute("authUser");

        if (isLoginAndProperUser(id, authUser)) {
            Blog blog = blogService.getBlog(id);
            List<Category> categories = categoryService.getCategories(blog.getUserId());

            model.addAttribute("blog", blog);
            model.addAttribute("categories", categories);
            return "blog/admin/blog-admin-write";
        }
        throw new BadRequestException();
    }

    @PostMapping(value = "/{id}/admin/write")
    public String blogAdminWritePost(@PathVariable("id") String id, @ModelAttribute Article article, HttpSession session, Model model) {
        User authUser = (User) session.getAttribute("authUser");
        if (isLoginAndProperUser(id, authUser)) {
            articleService.Write(article);
            return String.format("redirect:/blog/%s", id);
        }
        throw new BadRequestException();
    }

    @PostMapping(value = "/search")
    public String search(@ModelAttribute SearchCondition searchCondition) {
        try {
            String id = blogService.searchByRequest(searchCondition);
            return String.format("redirect:/blog/%s", id);
        } catch (RuntimeException e) {
            throw new NoContentException();
        }
    }

    private static boolean isLoginAndProperUser(String id, User authUser) {
        return authUser != null && id.equals(authUser.getId());
    }

    private static Article articleAtFirst(List<Article> articles) {
        return articles.get(0);
    }

    private static int getArticlesSize(List<Article> articles) {
        return Optional.ofNullable(articles)
                .map(List::size)
                .orElse(0);
    }

}
