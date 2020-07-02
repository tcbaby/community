function likeOrUnlike(btn, entityUserId, entityType, entityId)
{
    $.ajax({
        url: CONTEXT_PATH + "like",
        type: 'PUT',
        data: {
            "entityUserId": entityUserId,
            "entityType": entityType,
            "entityId": entityId,
        },
        success: function (data) {
            const childs = $(btn).children();
            $(childs[0]).text((data.likeStatus==1)?'已赞':'赞');
            $(childs[1]).text(data.totalLike);
        }
    })
}