<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/bootstrap.min.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome.min.css' />">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/jquery.bootgrid.min-1.3.1.css' />">

    <script src="<c:url value='/scripts/jquery-2.1.4.js'/>"></script>
    <script src="<c:url value='/scripts/jquery.bootgrid.min-1.3.1.js'/>"></script>
    <script src="<c:url value='/scripts/jquery.bootgrid.fa.min-1.3.1.js'/>"></script>

    <title>PO Box Picker</title>

    <script>
        var recognition = new webkitSpeechRecognition();
        var recognizing = false;

        // recognition.continuous = false;
        // recognition.interimResults = false;
        recognition.lang = "en-AU";
        recognition.onresult = function(event) {
            searchTerm = event.results[0][0].transcript;
            $("#searchTerm").val(searchTerm);
        }

        recognition.onend = function(event) {
            recognizing = false;
            $("#microphoneIcon").removeClass("fa-microphone-slash");
            $("#microphoneIcon").addClass("fa-microphone");
        }

        jQuery(document).ready(function ($) {
            $("#microphone").click(function(event) {
                if (recognizing) {
                    $("#microphoneIcon").addClass("fa-microphone");
                    $("#microphoneIcon").removeClass("fa-microphone-slash");
                    recognition.stop();
                    recognizing = false;
                } else {
                    $("#microphoneIcon").removeClass("fa-microphone");
                    $("#microphoneIcon").addClass("fa-microphone-slash");
                    recognition.start();
                    recognizing = true;
                }
            });

            $("#searchTerm").keypress(function(e) {
                var key = e.which;
                if (key == 13) {
                    $.get("<c:url value='/rest/box' />?searchTerm=" + $("#searchTerm").val(), function(data) {
                        alert("Got it " + $("#searchTerm").val());
                    });
                }
            });

            $("#boxes").bootgrid({
                navigation: 0,
                selection: true,
                rowSelect: true
            });
         });
    </script>
</head>

<body>
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="input-group">
                    <input type="text" class="form-control" id="searchTerm" placeholder="PO Box or Boxholder name" aria-describedby="microphone" />
                    <a id="microphone" class="btn input-group-addon" href="#"><span id="microphoneIcon" class="fa fa-microphone" /></a>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <table id="boxes" class="table table-condensed table-hover table-striped">
                    <thead>
                        <tr>
                            <th>Box</th>
                            <th>First Name</th>
                            <th>Last Name</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>Box</td>
                            <td>First Name</td>
                            <td>Last Name</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
