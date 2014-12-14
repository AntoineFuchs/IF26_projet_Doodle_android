<?php
	/* Fichier PHP de connexion d'un utilisateur */


	//*** Inclusion de la base de données ***//
	
	require_once('include/db.class.php');
	$db = DB::getInstance();
  
	//*** Récupération des paramètres ***//
	
	$parameters = array
	(
	':nom' => null,
	':prenom' => null,
	':email' => null,
	':password' => null,
	);

	foreach($_GET as $key => $value)
	{
	$parameters[":$key"] = $value;
	}

	$json = array(
	'error' => true
	);

	//*** Requête ***//
	
	$contact = $db->insertUser($parameters[":nom"],$parameters[":prenom"],$parameters[":email"],$parameters[":password"]);
	
	if ($contact !== false) {
	
		//Création du token
		
		$json = array(
	'error' => "bien ajouté"
	);
			
		
	}
	
	//Retourne le json
	echo json_encode($json);
	
?>
