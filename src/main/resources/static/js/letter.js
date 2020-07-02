$(function(){
	$("#sendBtn").click(send_letter);
	$(".close").click(delete_msg);
});

function send_letter() {
	// 发送请求
	let toUsername = $("#recipient-name").val();
	let content = $("#message-text").val();
	$.ajax({
		url: CONTEXT_PATH + "/letter",
		type: 'POST',
		data: {"toUsername": toUsername, "content": content},
		success: function (data) {
			showMessage("发送成功！");
			window.location.reload();
		},
		error: function () {
			showMessage("发送失败，用户不存在！");
		}
	})
}

function delete_msg() {
	// TODO 删除数据
	$(this).parents(".media").remove();
}

function showMessage(msg) {
	// 隐藏发送私信界面
	$("#sendModal").modal("hide");
	// 修改提示内容
	$("#hintBody").text(msg);
	// 弹出提示信息
	$("#hintModal").modal("show");
	// 2s后取消提示
	setTimeout(function(){
		$("#hintModal").modal("hide");
	}, 2000);
}