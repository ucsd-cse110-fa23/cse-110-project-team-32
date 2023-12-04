# PantryPal Team 32


Clone the repo

Create .vscode folder in root directory

In .vscode folder, create launch.json and paste the following, ensure you change your javaFX lib file path

	{

		"version": "0.2.0",
		
		"configurations": [

			{

				"type": "java",

				"name": "PantryPal",

				"request": "launch",

				"mainClass": "client.PantryPal",

				"vmArgs": "--module-path <path> --add-modules javafx.controls,javafx.fxml"

			},

			{

				"type": "java",

				"name": "Server",

				"request": "launch",

				"mainClass": "server.Server",

			},

		]

	}

	  

In .vscode folder, create settings.json file, and paste the following code

	{

		"java.configuration.updateBuildConfiguration": "automatic",

		"java.compile.nullAnalysis.mode": "automatic"

	}

How to run Pantry pal 2:

1. Go to "Run and Debug" tab.
2. Run the Server by picking Server option in the dropdown tab and click the run icon. If successfully done, the console will display "Server started on port 8100"
3. Run the Pantry pal 2 application by picking Pantry pal option in the dropdown tab and click the run icon. If successfully done, the application will launch.
4. In this stage, you are prompted to log in. Create an account if you haven't and log in if you haven't.
5. When you are logged-in you are able to see "Create new recipe", "Filter by", "Sort A-Z", "Sort Z-A", "Sort by latest", "Sort by earliest" and "Log out" button. Once your account has saved
   recipes, you are able to filter recipes according to the meal type and sort recipes according to alphabetical and chronological order.
6. When you click on the "Create new recipe" button, you are prompted to a new page with a "start recording" and "stop recording" button. 
   To create a new recipe, click on the recording button and answer the desired meal type prompt. Then click on stop recording. Do the same thing with the desired ingredients prompt.
   If done correctly, you will be brought into a new page where the application will generate a recipe according to your desired meal type and ingredients.
7. In the new page, you are able to regenerate the recipe if you are not satisfied by clicking the "regenerate recipe". You are also able to edit the instructions and ingredients of the recipe
   by clicking the edit button. Next to that, you are also able to save and cancel the recipe generated.
