<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="cpath" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메일 인증 예제</title>
<style>
	h4 {
		margin: 5px;
	}
	.box {
		width: 400px;
		box-sizing: border-box;
		display: flex;
		flex-flow: column;
		justify-content: center;
		border: 2px solid black;
		border-radius: 25px;
		margin-top: 10px;
		padding: 10px;
	}
	.hidden {
		display: none;
		font-size: 
	}
</style>
</head>
<body>

<h1>메일 인증 예제</h1>
<hr>

<form id="sendMailForm">
	<div class="box">
		<h4>이메일 주소 입력</h4>
		<div>
			<input type="email" name="email" placeholder="이메일 주소">
			<input type="submit" value="인증번호 전송">
		</div>
		<div id="sendMailMsg"></div>
	</div>
</form>

<form id="authMailForm" class="hidden">
	<div class="box">
		<h4>인증 번호 입력</h4>
		<div>
			<input type="text" name="auth" placeholder="인증 번호를 입력하세요">
			<input type="submit" value="인증">
		</div>
		<div id="authMailMsg"></div>
	</div>
</form>

<script>
	// 메일 주소를 입력하고 폼을 서브밋하면 작동할 이벤트
	const sendMailForm = document.getElementById('sendMailForm')
	const sendMailMsg = document.getElementById('sendMailMsg')
	
	
	const sendMailHandler = function(event) {
		event.preventDefault()
		const email = event.target.querySelector('input[name="email"]')
		const url = '${cpath}/mailto/' + email.value + '/'
		const opt = {
			method: 'GET'
		}
		fetch(url, opt).then(resp => resp.text())
		.then(text => {
			if(text.length == 128) {	// hash값을 받았다면 길이는 128이다
				sendMailMsg.innerText = '입력한 이메일로 인증번호를 전송했습니다'
				sendMailMsg.style.color = 'blue'
				sendMailMsg.style.fontWeight = 'bold'
				authMailForm.classList.remove('hidden')
			}
			else {						// error msg를 받았다면 128이 아니다
				sendMailMsg.innerText = text
				sendMailMsg.style.color = 'red'
				sendMailMsg.style.fontWeight = 'bold'
			}
		})
	}
	sendMailForm.onsubmit = sendMailHandler
	
	
	
	// 인증번호를 입력해서 결과를 확인하는 이벤트 함수
	const authMailForm = document.getElementById('authMailForm')
	const authMailMsg = document.getElementById('authMailMsg')
	const authHandler = function(event) {
		event.preventDefault()
		const userNumber = event.target.querySelector('input[name="auth"]')
		const url = '${cpath}/getAuthResult/' + userNumber.value
		const opt = {
			method: 'GET'
		}
		fetch(url, opt).then(resp => resp.text())
		.then(text => {
			console.log(text)
			if(text == 'true') {
				authMailMsg.innerText = '인증 성공 !!'
				authMailMsg.style.color = 'blue'
				authMailMsg.style.fontWeight = 'bold'
			}
			else {
				authMailMsg.innerText = '인증 실패 !!'
				authMailMsg.style.color = 'red'
				authMailMsg.style.fontWeight = 'bold'
			}
		})
	}
	authMailForm.onsubmit = authHandler
	
	
	
</script>

</body>
</html>