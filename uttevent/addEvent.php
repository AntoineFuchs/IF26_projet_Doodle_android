<?php
	/* Fichier PHP de transfert d'argent envers un membre */


	//*** Inclusion de la base de donn�es ***//
	
	require('include/db.class.php');
	$db = DB::getInstance();

	//*** R�cup�ration des param�tres ***//

	$parameters = array
	(
		
		':token' => '27429d67ca0d5bff67cdcef16bcb7a50',
		':sujet' => 'boite ddd',
		':description'=>'dance'
	);

	foreach($_GET as $key => $value)
	{
		$parameters[":$key"] = $value;
	}

	
	

	//*** Requ�te ***//
	
	$user = $db->getUserByToken($parameters[":token"]);

	if($user !== false && (time()-$user->heure) < 2700 ){
	     $db->insertEvent($user->ID,$parameters[':sujet'],$parameters[':description']);
	    $json='ajout�';
	}
    else {
	$json='session expir�e';
	}
	//Retourne le json
	echo $json;
?>