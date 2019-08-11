function setup() {
    var dateFilter = $('input[name="datefilter"]');


    dateFilter.daterangepicker({
        autoUpdateInput: false,
        locale: {
            cancelLabel: 'Clear'
        }
    });

    dateFilter.on('apply.daterangepicker', function(ev, picker) {
        $(this).val(picker.startDate.format('DD/MM/YYYY') + ' - ' + picker.endDate.format('DD/MM/YYYY'));

        var startDate = $('input[id="startDate"]');
        var endDate = $('input[id="endDate"]');

        startDate.val(picker.startDate.format('DD/MM/YYYY'));
        endDate.val(picker.endDate.format('DD/MM/YYYY'));
    });

    dateFilter.on('cancel.daterangepicker', function(ev, picker) {
        $(this).val('');
    });
}


$(document).ready(setup());