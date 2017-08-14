
<?php

  include 'config.php';

  if(isset($_GET["id"]) && !empty($_GET["id"])){
    $id = $_GET["id"];
  }else{
      $id = 0;
  }

  $sql = new mysqli($mysql_host.":".$mysql_port, $mysql_user, $mysql_password, $mysql_database);
  if($sql -> connect_error){
    die("Could not connect to MySQL server. Please contact an administrator. Error: ".$sql -> connect_error);
  }

  $ps = $sql->prepare("SELECT name,content FROM chatlogs WHERE id=?;");
  $ps->bind_param("s", $id);
  $ps->execute();

  $ps->store_result();
  $ps->bind_result($name, $content);

  if(!$ps->fetch()){
    $name = "Nicht gefunden";
    $content='[{"time":"", "message":"Der Chatlog wurde nicht gefunden"}]';
  }

  $decoded = json_decode($content, true);

  for($i = 0; $i < sizeof($decoded); $i++){
      $msg_array[$decoded[$i]["time"]] = htmlspecialchars($decoded[$i]["message"]);
  }


  $ps->close();
  $sql->close();

 ?>

 <!DOCTYPE html>
 <html>
   <head>
     <meta charset="utf-8">
     <title>Chatlog</title>
     <link rel="stylesheet" href="css/master.css">
     <link rel="stylesheet" type="text/css" href="http://fonts.googleapis.com/css?family=Ubuntu:regular,bold&subset=Latin">
     <link rel="icon" href="icon.png">
     <meta name="viewport" content="width=device-width, initial-scale=1.0">
   </head>
   <body>

     <div id="header">
       <a href="https://ToxicSponge.de/">TOXICSPONGE.DE</a>
     </div>

     <div class="container">
       <div class="head">Chatlog</div>
       <div class="content">

        <?php

          foreach($msg_array as $key=>$value){
            echo '<div class="entry">';
            echo '<span class="name">'.$name.': </span>';
            echo '<span class="message">'.$value.'</span>';

            if($key != 0){
              echo '<span class="time">['.(gmdate("H:i", $key + 2*60*60)).']</span>';
            }else{
              echo '<span class="time"></span>';
            }

            echo '</div>';
          }

         ?>

       </div>
     </div>

     <div id="footer">
         &copy; 2017 ToxicSponge.de<br>
         Coded by Max_Plays
     </div>

   </body>
 </html>
