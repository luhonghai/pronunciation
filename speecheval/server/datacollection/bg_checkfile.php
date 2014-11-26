<?php

   	$debug = false;	
	$config = require("config.php");
	$dataroot= $config['data_root'].'/data/';

	$flvname= $dataroot . (isset($_POST["flvname"])? $_POST["flvname"] : "") . '.flv';
	
	if($debug) {
		print($flvname."<br>");
	}
	
	if(file_exists($flvname)) {
		print("yes");
	}else {
		print("no");
	}


