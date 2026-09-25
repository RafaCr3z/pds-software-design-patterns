package menu;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import estairways.ClasseConforto;

/** Janela onde se pedem os dados da reserva durante a sua criação
 */
class VooReservarDialog {
	
	/** nome do tipo de reserva selecionada */
	private String reservada;
	
	/** construtor da janela com a info a mostrar
	 * @param owner janela mãe desta
	 * @param classe classe de conforto selecionada para a reserva
	 * @param numPassageiros número de passageiros a viajar
	 */
	public VooReservarDialog(Frame owner, ClasseConforto classe, int numPassageiros) {
		camposNomes = new ArrayList<JTextField>(numPassageiros); 
		janela = new JDialog( owner, true );
		janela.setSize( 500, 230 + 35*numPassageiros);
		janela.setContentPane( setupInterface(numPassageiros) );
		janela.setLocationRelativeTo( owner );
	}

	/** método a chamar para adicionar um novo tipo de reserva para o cliente poder
	 * escolher entre as várias disponíveis 
	 * @param nomeReserva nome do tipo de reserva
	 * @param precoReserva preço total se este tipo de reserva for o escolhido
	 */
	public void addTipoReserva(String nomeReserva, long precoReserva) {
		SpringLayout layout = new SpringLayout();
		JPanel panel = new JPanel( layout );
		panel.setBorder( BorderFactory.createBevelBorder(BevelBorder.LOWERED ) );
		JLabel nomeLbl = new JLabel( nomeReserva );
		nomeLbl.setFont(nomeFont);
		panel.add( nomeLbl );
		JLabel precoLbl = new JLabel( precoReserva + "€" );
		precoLbl.setFont( precoFont );
		panel.add( precoLbl );
		JButton reservarBt = new JButton( "Reservar" );
		panel.add(reservarBt);
		reservarBt.addActionListener( a -> reservar( nomeReserva ) );
		
		layout.putConstraint( SpringLayout.HORIZONTAL_CENTER, nomeLbl, 0, SpringLayout.HORIZONTAL_CENTER, panel );
		layout.putConstraint( SpringLayout.NORTH, nomeLbl, 5, SpringLayout.NORTH, panel );

		layout.putConstraint( SpringLayout.HORIZONTAL_CENTER, precoLbl, 0, SpringLayout.HORIZONTAL_CENTER, panel );
		layout.putConstraint( SpringLayout.NORTH, precoLbl, 5, SpringLayout.SOUTH, nomeLbl );

		layout.putConstraint( SpringLayout.HORIZONTAL_CENTER, reservarBt, 0, SpringLayout.HORIZONTAL_CENTER, panel );
		layout.putConstraint( SpringLayout.NORTH, reservarBt, 5, SpringLayout.SOUTH, precoLbl );
		
		layout.putConstraint( SpringLayout.SOUTH, panel, 5, SpringLayout.SOUTH, reservarBt );

		
		painelExperiencias.add( panel );
	}	

	/** método chamado quando se pressiona obotão reservar num tipo de reserva
	 * @param nomeReserva nome da reserva selecionada, ou null se a reserva foi cancelada
	 */
	private void reservar(String nomeReserva) {
		if( !checkNomesPassageiros() ) {
			JOptionPane.showMessageDialog(janela, "Por favor, preencha os nomes todos");
			return;
		}
		this.reservada = nomeReserva;
		janela.setVisible( false );
	}

	/** verifica se todos os nomes estão corretamente introduzidos
	 * @return true se todos estiverem corretos, false caso contrário
	 */
	private boolean checkNomesPassageiros() {
		return camposNomes.stream().noneMatch( tf -> tf.getText().isBlank() );
	}
	
	/** método que apresenta a janela de reservar */
	public String reservarVoo( ) {
		janela.setVisible( true );
		return reservada;
	}
	
	/** indica qual o nome da reerva selecionada, ou null se a reserva foi cancelada
	 * @return o nome da reerva selecionada, ou null se a reserva foi cancelada
	 */
	public String getNomeReserva() {
		return reservada;
	}
	
	/** retorna uma lista com os nomes dos passageiros
	 * @return uma lista com os nomes dos passageiros
	 */
	public List<String> getNomes( ) {
		return camposNomes.stream().map( JTextField::getText ).toList();
	}

	// variáveis para os elementos gráficos
	private JDialog janela;
	private List<JTextField> camposNomes; 
	private JPanel painelExperiencias;
	private Font nomeFont = new Font( "Arial", Font.BOLD, 16 );
	private Font precoFont = nomeFont.deriveFont( nomeFont.getSize() - 2 );

	
	private JPanel setupInterface( int numPassageiros ) {
		JPanel painel = new JPanel( new BorderLayout() );
		SpringLayout layout = new SpringLayout();
		JPanel passaPanel = new JPanel( layout );
		passaPanel.setBorder( new TitledBorder("Dados dos passageiros") );
		JLabel lastLabel = null;
		for( int i=0; i< numPassageiros; i++ ) {
			JLabel nomeLbl = new JLabel("Nome passageiro " + (i+1) + ":");
			passaPanel.add( nomeLbl );
			JTextField nomeTf = new JTextField("");
			camposNomes.add( nomeTf );
			
			passaPanel.add( nomeTf );
			if( lastLabel == null )
				layout.putConstraint( SpringLayout.NORTH, nomeLbl, 5, SpringLayout.NORTH, passaPanel );
			else
				layout.putConstraint( SpringLayout.NORTH, nomeLbl, 10, SpringLayout.SOUTH, lastLabel );
			layout.putConstraint( SpringLayout.WEST, nomeLbl, 5, SpringLayout.WEST, passaPanel );
			layout.putConstraint( SpringLayout.WEST, nomeTf, 8, SpringLayout.EAST, nomeLbl );
			layout.putConstraint( SpringLayout.EAST, nomeTf, -5, SpringLayout.EAST, passaPanel );
			layout.putConstraint( SpringLayout.BASELINE, nomeTf, 5, SpringLayout.BASELINE, nomeLbl );
			lastLabel = nomeLbl;
		}		
		layout.putConstraint( SpringLayout.SOUTH, passaPanel, 6, SpringLayout.SOUTH, lastLabel );
		layout.putConstraint( SpringLayout.WEST, passaPanel, -500, SpringLayout.EAST, passaPanel );
		
		painel.add( passaPanel, BorderLayout.NORTH );
		painelExperiencias = new JPanel( new GridLayout( 1, 0 ) );
		painelExperiencias.setBorder( new TitledBorder("Escolha a sua experiência") );
		painel.add( painelExperiencias, BorderLayout.CENTER );
		return painel;
	}

}