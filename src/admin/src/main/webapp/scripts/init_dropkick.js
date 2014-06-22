$(document).ready( function() {
    $('select[id!="subjectmatters"]').dropkick({
        change: function (value, label) {
            $(this).change();
        }
    });
});