<?php
	/* Fichier PHP de transfert d'argent envers un membre */


	//*** Inclusion de la base de données ***//
	
	require('include/db.class.php');
	$db = DB::getInstance();

	//*** Récupération des paramètres ***//

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

	
	

	//*** Requête ***//
	
	$user = $db->getUserByToken($parameters[":token"]);

	if($user !== false && (time()-$user->heure) < 2700 ){
	     $db->insertEvent($user->ID,$parameters[':sujet'],$parameters[':description']);
	    $json='ajouté';
	}
    else {
	$json='session expirée';
	}
	//Retourne le json
	echo $json;
?>