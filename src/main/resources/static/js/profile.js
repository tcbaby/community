function follow(btn, userId) {
	$.ajax({
		url: CONTEXT_PATH + "follow",
		type: "PUT",
		data: {
			"entityType": 3,
			"entityId": userId
		},
		success: function (data) {
			// 修改粉丝数
			$("#followers").text(data.totalFollowers);
			// 修改关注/取关按钮
			if (data.followStatus == 1) {
				$(btn).text("已关注").removeClass("btn-info").addClass("btn-secondary");
			} else {
				$(btn).text("关注TA").removeClass("btn-secondary").addClass("btn-info");
			}
		}
	});
}