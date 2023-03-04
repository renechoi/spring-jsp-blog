package kosta.springjspblog.service;

import java.util.List;

import kosta.springjspblog.domain.Article;
import kosta.springjspblog.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ArticleService {

	private final ArticleRepository articleRepository;

	/*포스트 글저장*/
	public Article Write(Article article) {
		return articleRepository.save(article);
	}

	/*특정 회원의 선택한 카테고리의 포스트 리스트 가져오기*/
	public List<Article> getArticles(int userNo, int crtCateNo) {
		return articleRepository.findAll(userNo, crtCateNo);
	}

	/*선택한 포스트 가져오기*/
	public Article getArticle(int articleNo) {
		return articleRepository.findById(String.valueOf(articleNo));
	}
}