$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	// 隐藏发布帖子界面
	$("#publishModal").modal("hide");
	// 提交发布的贴子信息
	let title = $("#recipient-name").val();
	let content = $("#message-text").val();
	$.ajax({
		url: CONTEXT_PATH + "discuss/",
		type: "POST",
		data: {title: title, content: content},
		success: function (data) {
			showMessage("发布成功！");
			window.location.reload();
		},
		error: function () {
			showMessage("发布失败，请稍后重试！");
		}
	})
}

function showMessage(msg) {
	// 修改提示内容
	$("#hintBody").text(msg);
	// 弹出提示信息
	$("#hintModal").modal("show");
	// 2s后取消提示
	setTimeout(function(){
		$("#hintModal").modal("hide");
	}, 2000);
}