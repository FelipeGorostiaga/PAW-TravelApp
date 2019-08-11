function rate_post(url, rate){
    $.post(url + rate, function (data) {
        console.log(data);
    })
        .fail(function () {
            alert("fail");
        });
}

function init_rate(update_url, rate, traveling_or_admin) {
    var options = {
        initial_value: rate,
        step_size: 1,
        readonly: !traveling_or_admin
    };

    var rate_bar = $('#rate-bar');

    rate_bar.rate(options);
    rate_bar.on("change", function (ev, data) {
        rate_post(update_url, data.to)
    })
}

