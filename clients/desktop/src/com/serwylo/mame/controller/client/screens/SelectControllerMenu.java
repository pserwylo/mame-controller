package com.serwylo.mame.controller.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.TableLayout;
import com.serwylo.mame.controller.client.ControllerLayout;
import com.serwylo.mame.controller.client.MameControllerClient;

import java.util.ArrayList;

public class SelectControllerMenu implements Screen, ClickListener
{

	private static SelectControllerMenu singleton;

	public static SelectControllerMenu getInstance( MameControllerClient app )
	{
		if ( singleton == null )
		{
			singleton = new SelectControllerMenu( app );
		}
		return singleton;
	}

	private Stage stage;

	/**
	 * Required so that we can ask for other screens to be displayed.
	 */
	private MameControllerClient app;

	/**
	 * Keep references to the menu buttons so that we can use ourself as a
	 * click listener.
	 */
	private TextButton backButton;

	public SelectControllerMenu( MameControllerClient app )
	{        
		this.app = app;
	}
	
	@Override
	public void click( Actor actor, float x, float y )
	{
		if ( actor == this.backButton )
		{
			this.app.setScreen( MainMenu.getInstance( this.app ) );
		}
	}
	
	@Override
	public void render( float delta ) 
	{
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        stage.act( Gdx.graphics.getDeltaTime() );
        stage.draw();
        // Table.drawDebug( stage );
	}

	@Override
	public void resize( int width, int height ) 
	{
		if ( this.backButton == null )
		{
			this.createUi( width, height );
		}
	}
	
	private void createUi( int width, int height )
	{
        this.stage = new Stage( width, height, true );
        Gdx.input.setInputProcessor( this.stage );
        
        Skin skin = new Skin( Gdx.files.internal( "ui/uiskin.json" ), Gdx.files.internal( "ui/uiskin.png") );
        
        Table table = new Table( skin );
        table.width = width;
        table.height = height;
        this.stage.addActor( table );
        
        TableLayout layout = table.getTableLayout();

		ArrayList<FileHandle> controllers = ControllerLayout.findControllers();
		for ( FileHandle controller : controllers )
		{
			String controllerName = controller.nameWithoutExtension();
			TextButton controllerButton = new TextButton( controllerName, skin.getStyle( TextButtonStyle.class ), controllerName + "Button" );
			controllerButton.setClickListener( this );
			controllerButton.size( 400, 80 );
			layout.add( controllerButton );

			layout.add( new Table() );
		}

        this.backButton = new TextButton( "Back", skin.getStyle( TextButtonStyle.class ), "backButton" );
        this.backButton.setClickListener( this );
        layout.register( this.backButton );

        layout.parse( Gdx.files.internal( "ui/screens/select-controller.txt" ).readString() );
	}

	@Override
	public void show() 
	{
        Gdx.input.setInputProcessor( this.stage );
	}

	@Override
	public void hide() 
	{

	}

	@Override
	public void pause() 
	{

	}

	@Override
	public void resume() 
	{

	}

	@Override
	public void dispose() 
	{
	}

}
