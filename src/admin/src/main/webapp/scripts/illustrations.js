$(document).ready( function() {
    $(':input:visible:enabled:first').focus();

    $("#btn_back_image").click( function( e ) {
        var img = $("#imageId").val();
        var url = "illustrations.htm";
        if(typeof img !== 'undefined')
        {
            url += "?select_image=" + img;
        }
        $(location).attr('href',url);
    })

    $("#btn_back_map").click( function( e ) {
        var map = $("#mapId").val();
        var url = "illustrations.htm";
        if(typeof map !== 'undefined')
        {
            url += "?select_map=" + map;
        }
        $(location).attr('href',url);
    })

    $("#add_image_table_url_input").change(function() {
        $("#show_image").attr('href', $(this).val());
        check_url();
    })

    $("#add_map_table_url_input").change(function() {
        $("#show_map").attr('href', $(this).val());
        check_url();
    })

    check_url();
});

function check_url() {
    if( $('[id^="show_"]').attr('href') != undefined) {
        if( $('[id^="show_"]').attr('href').match(/^http(s|):\/\/maps\.google\.com.+$/) ) {
            $('[id^="show_"]').hide();
        } else {
            $('[id^="show_"]').show();
        }
    }
}