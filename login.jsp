<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="./css/textStyle.css">
<link rel="stylesheet" type="text/css" href="./css/table.css">
<link rel="stylesheet" type="text/css" href="./css/formStyle.css">
<title>ログイン画面</title>
</head>
<body>
	<div id="header">
		<jsp:include page="header.jsp" />
	</div>

	<div id="main">
		<div id="top">
			<p>ログイン画面</p>
		</div>
		<s:if
			test="userIdErrorMessageList != null && userIdErrorMessageList.size()>0">
			<div id="error">
				<div id="error-message">
					<s:iterator value="userIdErrorMessageList">
						<s:property />
						<br>
					</s:iterator>
				</div>
			</div>
		</s:if>
		<s:if
			test="passwordErrorMessageList != null && passwordErrorMessageList.size()>0">
			<div id="error">
				<div id="error-message">
					<s:iterator value="passwordErrorMessageList">
						<s:property />
						<br>
					</s:iterator>
				</div>
			</div>
		</s:if>
		<s:if
			test="isNotUserInfoMessage != null && !isNotUserInfoMessage.isEmpty()">
			<div id="error">
				<div id="error-message">
					<s:property value="isNotUserInfoMessage" />
				</div>
			</div>
		</s:if>
		<s:form action="LoginAction">
			<table class="vertical">
				<tr>
					<th><label>ユーザーID</label></th>
					<s:if test="#session.saveUserFlg==true">
						<td><s:textfield name="userId" class="textbox"
								value='%{#session.userId}' placeholder="ユーザーID" /></td>
					</s:if>
					<s:else>
						<td><s:textfield name="userId" class="textbox"
								placeholder="ユーザーID" value='%{userId}' /></td>
					</s:else>
				</tr>
				<tr>
					<th><label>パスワード</label></th>
					<td><s:password name="password" class="textbox"
							placeholder="パスワード" /></td>
				</tr>
			</table>

			<div id="box">
				<s:if
					test="(#session.saveUserFlg==true && #session.userId!=null && !#session.userId.isEmpty()) || saveUserFlg==true">
					<s:checkbox name="saveUserFlg" checked="checked" />
				</s:if>
				<s:else>
					<s:checkbox name="saveUserFlg" />
				</s:else>
				<s:label value="ユーザーID保存" />
				<br>
			</div>

			<div class="submit_box">
				<s:submit value="ログイン" class="submit_btn" />
			</div>
		</s:form>

		<s:form action="CreateUserAction">
			<div class="submit_box">
				<s:submit value="新規ユーザ登録" class="submit_btn" />
			</div>
		</s:form>
		<s:form action="ResetPasswordAction">
			<div class="submit_box">
				<s:submit value="パスワード再設定" class="submit_btn" />
			</div>
		</s:form>
	</div>

</body>
</html>