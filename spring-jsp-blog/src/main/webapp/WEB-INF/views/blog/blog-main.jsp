<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%> 


<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JBlog</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/jblog.css">
<script type="text/javascript" src="${pageContext.request.contextPath }/assets/js/jquery/jquery-1.12.4.js"></script>
</head>
<body>

	<div id="container">

		<!-- 블로그 해더 -->
		<c:import url="/WEB-INF/views/includes/blog-header.jsp"></c:import>
		
		<div id="wrapper">
			<div id="content">


				<div class="blog-content">
					<c:if test="${not empty article}">
						<h4>${article.articleTitle}</h4>
						<p>
						${fn:replace(article.articleContent, newLine, "<br>") }
						<p>
					</c:if>
					<c:if test="${empty article}">
						<h4>등록된 글이 없습니다.</h4>
						<p>
						
						<p>
					</c:if>
				</div>
				<ul class="blog-list">
					<c:forEach items="${articles}" var="article">
						<li>
							<a href="${pageContext.request.contextPath}/blog/${blog.userId}?categoryNo=${article.categoryNo}&articleNo=${article.articleNo}">${article.articleTitle}</a>
							<span>${article.regDate}</span>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>

		<div id="extra">
			<div class="blog-logo">
				<c:if test="${blog.logoFile eq 'default'}">
					<img src="${pageContext.request.contextPath}/assets/images/spring-logo.jpg">
				</c:if>
				<c:if test="${blog.logoFile ne 'default'}">
					<img src="${pageContext.request.contextPath}/upload/${blog.logoFile}">
<%--					<img src="${pageContext.request.contextPath}/resources/upload/1678091707628636c81a3-3b82-434c-add7-97e46bcaca72.jpeg">--%>
				</c:if>
			</div>
		</div>

		<div id="navigation">
			<h2><a href="${pageContext.request.contextPath}/blog/${blog.userId}">카테고리</a></h2>
			<ul>
				<c:forEach items="${categories}" var="category">
					<li><a href="${pageContext.request.contextPath}/blog/${blog.userId}?categoryNo=${category.categoryNo}">${category.categoryName}</a></li>
				</c:forEach>
			</ul>
		</div>
		
		<!-- 푸터-->
		<c:import url="/WEB-INF/views/includes/blog-footer.jsp"></c:import>
		
	</div>
</body>
</html>