<?php
	/* Fichier PHP de connexion d'un utilisateur */


	//*** Inclusion de la base de données ***//
	
	require_once('include/db.class.php');
	$db = DB::getInstance();
  
	//*** Récupération des paramètres ***//
	
	$parameters = array
	(
	':email' => "hamza.d@g.com",
	':password' => "pass"
	);

	foreach($_GET as $key => $value)
	{
	$parameters[":$key"] = $value;
	}

	$json = array(
	'error' => true
	);

	//*** Requête ***//
	
	$contact = $db->getUserByLogin($parameters[":email"],$parameters[":password"]);
	
	if ($contact !== false) {
	
		//Création du token
		$token = md5(time() . $contact->email . $contact->password);
		
			
		if($db->updateToken($token,time(),$contact->ID))
		{
			unset($contact->email);
		    unset($contact->password);
			unset($contact->heure);
			unset($contact->token);
			
			$json = array(
				'error' => false,
				'token' => $token,
				'membre'=> $contact,
				); 
		
			
		}
	}
	
	//Retourne le json
	echo json_encode($json);
	
?>
