var count = 1;
$(document).ready( function() {
    var clicks = 0;
    var x1, y1, xPrev, yPrev;
    var inputX, inputY, inputAngle, inputId;

    update_map();

    $(':input:visible:enabled:first').focus();

    generate_link(false);
    
    if($("#select_map").val() == -1) {
        $('#open_map_dialog').hide();
    } else {
        var id = $("#select_map").val();
        if($('span[id="map-' + id + '"]').text() == 'true') {
            $('#open_map_dialog').hide();
        }
    }

    $('select[name="image"]').change(function() {
        generate_link();
    });

    $("#btn_back_library").click( function( e ) {
        var lib = $("#select_library").val();
        var url = "locations.htm";
        if(typeof lib !== 'undefined')
        {
            url += "?select_library=" + lib;
        }
        $(location).attr('href',url);
    })

    $("#btn_back_collection").click( function( e ) {
        var lib = $("#select_library").val();
        var col = $("#select_collection").val();
        var url = "locations.htm?select_library=" + lib;
        if(typeof col !== 'undefined')
        {
            url += "&select_collection=" + col;
        }
        $(location).attr('href',url);
    })

    $("#btn_back_shelf").click( function( e ) {
        var lib = $("#select_library").val();
        var col = $("#select_collection").val();
        var shelf = $("#select_shelf").val();

        var url = "locations.htm?select_library=" + lib + "&select_collection=" + col;
        if(typeof shelf !== 'undefined')
        {
            url += "&select_shelf=" + shelf;
        }
        $(location).attr('href',url);
    })

    $("#map_div").mousemove( function( e ) {
        var offset = $("#map").offset();
        var x = e.pageX - offset.left;
        var y = e.pageY - offset.top;
        var maxX = $("#map").css("width").replace('px','');
        var maxY = $("#map").css("height").replace('px','');
        if(x < maxX && y < maxY)
        {
            $("#x-coordinate").text( x.toFixed(0) );
            $("#y-coordinate").text( y.toFixed(0) );
        }

    });

    $("#map_div").click( function( e ) {
        var offset = $("#map").offset();
        var x = e.pageX - offset.left;
        var y = e.pageY - offset.top;
        var maxX = $("#map").css("width").replace('px','');
        var maxY = $("#map").css("height").replace('px','');

        if(x < maxX && y < maxY)
        {
            clicks++;

            if(clicks % 2 == 0)
            {
                if(xPrev > x) {
                    var tempX = x;
                    x = xPrev;
                    xPrev = tempX;
                    x1 = e.pageX;
                }
                if(yPrev > y) {
                    var tempY = y;
                    y = yPrev;
                    yPrev = tempY;
                    y1 = e.pageY;
                }

                $("img#pin").remove();               
                draw(0, xPrev.toFixed(0), yPrev.toFixed(0), x.toFixed(0), y.toFixed(0), 0);
                $("#map").remove();

                var url = get_map_url();
                var img = $("<img>");
                img.attr('id', 'map');
                img.attr('alt','');
                img.attr('src', url);
                $("#map_div").append(img);
            } else {
                xPrev = e.pageX - offset.left;
                yPrev = e.pageY - offset.top;

                var div_offset = $("#map_popup").offset();

                var pin = $('<img>');
                pin.attr('id', 'pin');
                pin.attr('src', 'images/pin.png');
                pin.css({
                    'position':'absolute',
                    top: y - (div_offset.top - offset.top) - 16, // 16px is the size of the pin icon
                    left: x - (div_offset.left - offset.left)
                });                
                $("#map_div").append(pin);
            }
            x1 = e.pageX;
            y1 = e.pageY;

            $("#x-coordinate1").text( x.toFixed(0) );
            $("#y-coordinate1").text( y.toFixed(0) );
        }
    });

    $("#select_map").change( function ( e ) {
        count = 1;
        clicks = 0;
        $("#x-coordinate").text( "" );
        $("#y-coordinate").text( "" );
        $("#x-coordinate1").text( "" );
        $("#y-coordinate1").text( "" );
        $("#remove_area").empty();
        $("#area_container").empty();
        $("#map_div").empty();
        $("#used_areas").val("");

        var id = $("#select_map").val();
        if(id != -1 && $('span[id="map-' + id + '"]').text() == 'false')
        {
            var img = $("<img>");
            img.attr('id', 'map');
            img.attr('alt','');
            img.attr('src', 'ImageCreator?mapId=' + $("#select_map").val());
            img.load(function() {
                $map_dialog.dialog( "option", "width", (this.width + 150) );
            });
            $("#map_div").append(img);
            $('#open_map_dialog').show();
        } else {
            $('#open_map_dialog').hide();
        }
    });
    
    var $map_dialog = $( "#map_popup" ).dialog({
        autoOpen: false,
        modal: true,
        buttons: {
            Close: function() {
                $( this ).dialog( "close" );
            }
        }
    });
    
    $('#open_map_dialog').click(function() {
        $map_dialog.dialog('open');
    });
    
    $('img').load(function() {
        $map_dialog.dialog( "option", "width", (this.width + 150) );
    });
});

function update_map()
{
    if($("#select_map").val() != -1)
    {
        if($("#map") != null)
        {
            $("#map").remove();
        }

        var url = get_map_url();
        var img = $("<img>");
        img.attr('id', 'map');
        img.attr('alt','');
        img.attr('src', url);
        $("#map_div").append(img);
    }
}

function get_map_url()
{
    var url = 'ImageCreator?';
    var params = 'mapId=' + $("#select_map").val();
    params += "&used_areas=" + $("#used_areas").val();
    var vals = $("#used_areas").val().split("|");
    for(var i=0; i < vals.length-1; i++ ) {
        params += "&x1_" + vals[i] + "=" + $("#x1_" + vals[i]).val();
        params += "&y1_" + vals[i] + "=" + $("#y1_" + vals[i]).val();
        params += "&x2_" + vals[i] + "=" + $("#x2_" + vals[i]).val();
        params += "&y2_" + vals[i] + "=" + $("#y2_" + vals[i]).val();
        params += "&angle_" + vals[i] + "=" + $("#angle_" + vals[i]).val();
    }
    url += params
    return url;
}

function draw(id, x1, y1, x2, y2, angle)
{
    var inp_count = count;
    var remove = $('<div>');
    remove.attr("id", "div_area" + count);
    remove.attr("class", "delete");
    remove.html("Area " + count + " <img src='images/delete.png' class='icon-delete' title='Delete area' />");
    $("#remove_area").append(remove);
    remove.click( function (e) {
        $("#arrow").remove();
        $("#div_area" + inp_count).remove();
        $("#areaId_" + inp_count).remove();
        $("#x1_" + inp_count).remove();
        $("#y1_" + inp_count).remove();
        $("#x2_" + inp_count).remove();
        $("#y2_" + inp_count).remove();
        $("#angle_" + inp_count).remove();
        $("#angle_div" + inp_count).remove();
        $("#used_areas").val($("#used_areas").val().replace(inp_count + "|", ""));
        update_map();
    });

    remove.on('mouseover', function () {
        var position = $("#map").position();
        var arrow = $('<img>');
        arrow.attr('id', 'arrow');
        arrow.attr('src', 'images/arrow.png');
        arrow.css({
            'position':'absolute',
            top: parseInt(y1) + position.top - 16,
            left: parseInt(x1) + position.left - 16
        });
        $("#map_div").append(arrow);
    });

    remove.on('mouseout', function () {
        $("#arrow").remove();
    });

    var angleDiv = $('<div>');
    angleDiv.attr("id", "angle_div" + count);
    angleDiv.attr("class", "angle_div");
    var angleInput = $('<input>');
    angleInput.attr("type", "text");
    angleInput.attr("id", "angle_input" + count);
    angleInput.attr("size", "2");
    angleInput.attr("value", angle);
    angleDiv.html("Angle: ");
    angleDiv.append(angleInput);
    $("#remove_area").append(angleDiv);
    
    angleInput.on('keyup', function() {
        $("#angle_" + inp_count).val($(this).val());
        update_map();
    });
    
    $("#used_areas").val($("#used_areas").val() + count + "|");

    inputId = $('<input>');
    inputId.attr("type", "hidden");
    inputId.attr("id", "areaId_" + count);
    inputId.attr("name", "areaId_" + count);
    inputId.attr("value", id);
    
    $('#area_container').append(inputId);
    
    inputX = $('<input>');
    inputX.attr("type", "hidden");
    inputX.attr("id", "x1_" + count);
    inputX.attr("name", "x1_" + count);
    inputX.attr("value", x1);

    inputY = $('<input>');
    inputY.attr("type", "hidden");
    inputY.attr("id", "y1_" + count);
    inputY.attr("name", "y1_" + count);
    inputY.attr("value", y1);

    $('#area_container').append(inputX);
    $('#area_container').append(inputY);

    inputX = $('<input>');
    inputX.attr("type", "hidden");
    inputX.attr("id", "x2_" + count)
    inputX.attr("name", "x2_" + count);
    inputX.attr("value", x2);

    inputY = $('<input>');
    inputY.attr("type", "hidden");
    inputY.attr("id", "y2_" + count);
    inputY.attr("name", "y2_" + count);
    inputY.attr("value", y2);

    $('#area_container').append(inputX);
    $('#area_container').append(inputY);
    
    inputAngle = $('<input>');
    inputAngle.attr("type", "hidden");
    inputAngle.attr("id", "angle_" + count);
    inputAngle.attr("name", "angle_" + count);
    inputAngle.attr("value", angle);

    $('#area_container').append(inputAngle);

    count++;
}

function generate_link() {
    var id = $('select[name="image"]').val();
    if(id != '-1') {
        var path = $('#images_path').text();
        var link = $('#image-' + id).text();
        if(!link.match(/^http.+/)) {
            link = path + link;
        }
        $('#show_image').attr('href', link);
        $('#show_image').show();
    } else {
        $('#show_image').hide();
    }
}