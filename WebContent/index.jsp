<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/jquery-1.11.1.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/ckeditor/ckeditor.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/ckfinder/ckfinder.js"></script>
</head>
<body>
	<form id="detailForm" method="post">
		<textarea id="content" name="content"></textarea>
		<input type="button" value="保存" id="save" onclick="save1()" />
	</form>
</body>
<script type="text/javascript">
	var editor = null;
	window.onload = function() {
		//实例化ckeditor
		editor = CKEDITOR.replace('content', {
			filebrowserImageUploadUrl:"${pageContext.request.contextPath}/common/upload/uploadImg.do",
			uiColor : '#ffffff',
			language : 'zh-cn',
			/* width : '500px',
			height : '500px', */
			on : {
				instanceReady : function(ev) {
					this.dataProcessor.writer.setRules('p', {
						indent : false,
						breakBeforeOpen : false, //<p>之前不加换行
						breakAfterOpen : false, //<p>之后不加换行
						breakBeforeClose : false, //</p>之前不加换行
						breakAfterClose : false
					//</p>之后不加换行7
					});
				}
			}
		}); //参数‘content’是textarea元素的name属性值，而非id属性值
		CKFinder.setupCKEditor(editor,
				'${pageContext.request.contextPath}/static/ckfinder/');
	}
	function save1() {
		editor.updateElement();
		//前台验证工作
		//提交到后台
		$.post('${pageContext.request.contextPath }/common/test1', {
			'content' : $('#content').val()
		}, function(data, status, xhr) {
			alert(status + "");
		}, "json");
	}
</script>
</html>