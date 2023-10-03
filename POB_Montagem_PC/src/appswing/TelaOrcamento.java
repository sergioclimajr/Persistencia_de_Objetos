/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * POB - Persistencia de Objetos
 * Prof. Fausto Ayres
 *
 */
package appswing;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import daodb4o.DAOComponente;
import modelo.Componente;
import modelo.Orcamento;
import regras_negocio.Fachada;

public class TelaOrcamento {
	private JDialog frame;
	private JTable table;
	private JScrollPane scrollPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JButton button;
	private JButton button_1;
	private JButton button_2;
	private JButton button_3;
	private JButton button_4;
	private JButton button_5;
	private JLabel label;
	private JLabel label_1;
	private JLabel label_2;
	private JLabel label_3;
	private JLabel label_4;
	DecimalFormat df = new DecimalFormat("#.00");
	Random gerador = new Random();
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaOrcamento tela = new TelaOrcamento();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TelaOrcamento() {
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JDialog();
		frame.setModal(true);
		frame.setResizable(false);
		frame.setTitle("Orçamento");
		frame.setBounds(100, 100, 730, 425);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				Fachada.inicializar();
				listagem();
			}
			@Override
			public void windowClosing(WindowEvent e) {
				Fachada.finalizar();
			}
		});

		scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 43, 674, 148);
		frame.getContentPane().add(scrollPane);

		table = new JTable();
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				label_3.setText("selecionado="+ (int) table.getValueAt( table.getSelectedRow(), 0));
			}
		});
		
		table.setGridColor(Color.BLACK);
		table.setRequestFocusEnabled(false);
		table.setFocusable(false);
		table.setBackground(new Color(144, 238, 144));
		table.setFillsViewportHeight(true);
		table.setRowSelectionAllowed(true);
		table.setFont(new Font("Tahoma", Font.PLAIN, 14));
		scrollPane.setViewportView(table);
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setShowGrid(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		label = new JLabel("");		//label de mensagem
		label.setForeground(Color.BLUE);
		label.setBounds(12, 355, 688, 14);
		frame.getContentPane().add(label);

		label_3 = new JLabel("resultados:");
		label_3.setBounds(21, 190, 431, 14);
		frame.getContentPane().add(label_3);

		label_1 = new JLabel("Data:");
		label_1.setHorizontalAlignment(SwingConstants.LEFT);
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_1.setBounds(20, 269, 89, 14);
		frame.getContentPane().add(label_1);

		textField = new JTextField();
		textField.setFont(new Font("Dialog", Font.PLAIN, 12));
		textField.setColumns(10);
		textField.setBounds(60, 266, 185, 20);
		frame.getContentPane().add(textField);
		
		
		label_4 = new JLabel("ID Componente:");
		label_4.setHorizontalAlignment(SwingConstants.LEFT);
		label_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_4.setBounds(430, 300, 95, 14);
		frame.getContentPane().add(label_4);
		
		textField_2 = new JTextField();
		textField_2.setFont(new Font("Dialog", Font.PLAIN, 12));
		textField_2.setColumns(10);
		textField_2.setBounds(525, 300, 75, 20);
		frame.getContentPane().add(textField_2);
		
		
		
		
		
		
		button_4 = new JButton("+  Add");
		button_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					if (table.getSelectedRow() >= 0){
						int idOrc = (int) table.getValueAt( table.getSelectedRow(), 0);
						int idComp = 0;
						try {
						    idComp = Integer.parseInt(textField_2.getText());
						} catch (NumberFormatException ex) {
						    label.setText("ID do componente inválido. Insira um número inteiro.");
						    return;
						}
						Fachada.adicionarCompAoOrcamento(idOrc, idComp);
						label.setText("Componente adicionado ao orçamento.");
						listagem();
					}
				}
				catch(Exception erro) {
					label.setText(erro.getMessage());
				}
			}
		});
		button_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		button_4.setBounds(610, 285, 75, 20);
		frame.getContentPane().add(button_4);
		
		
		
		
		
		button_5 = new JButton("-  Rem");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					if (table.getSelectedRow() >= 0){
						int idOrc = (int) table.getValueAt( table.getSelectedRow(), 0);
						int idComp = 0;
						try {
						    idComp = Integer.parseInt(textField_2.getText());
						} catch (NumberFormatException ex) {
						    label.setText("ID do componente inválido. Insira um número inteiro.");
						    return;
						}
						Fachada.removerCompDoOrcamento(idOrc, idComp);
						label.setText("Componente removido do orçamento.");
						listagem();
					}
				}
				catch(Exception erro) {
					label.setText(erro.getMessage());
				}
			}
		});
		button_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		button_5.setBounds(610, 310, 75, 20);
		frame.getContentPane().add(button_5);
		
		
		
		
		
		
		
		
		
		
		

		button_1 = new JButton("Criar novo Orçamento");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(textField.getText().isEmpty() || textField_1.getText().isEmpty()) {
						label.setText("campo vazio");
						return;
					}
					String dataText = textField.getText();
					String cpfCliente = textField_1.getText();

					Fachada.criarOrcamento(dataText, cpfCliente);
					label.setText("Orçamento criado");
					listagem();
				}
				catch(Exception ex) {
					label.setText(ex.getMessage());
				}
			}
		});
		button_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		button_1.setBounds(60, 330, 153, 23);
		frame.getContentPane().add(button_1);

		button = new JButton("Listar");
		button.setFont(new Font("Tahoma", Font.PLAIN, 12));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listagem();
			}
		});
		button.setBounds(308, 11, 89, 23);
		frame.getContentPane().add(button);

		
		button_2 = new JButton("Apagar selecionado");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					if (table.getSelectedRow() >= 0) {	
						int idOrc = (int) table.getValueAt( table.getSelectedRow(), 0);

						Fachada.excluirOrcamento(idOrc);
						label.setText("Orçamento apagado" );
						listagem();

					}
					else
						label.setText("nao selecionado");
				}
				catch(Exception ex) {
					label.setText(ex.getMessage());
				}
			}
		});
		button_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		button_2.setBounds(540, 200, 150, 23);
		frame.getContentPane().add(button_2);


		textField_1 = new JTextField();
		textField_1.setBounds(113, 297, 133, 19);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);

		JTextPane textPane = new JTextPane();
		textPane.setBounds(47, 308, 1, 16);
		frame.getContentPane().add(textPane);

		label_2 = new JLabel("CPF do Cliente:");
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_2.setBounds(20, 295, 89, 16);
		frame.getContentPane().add(label_2);

		
		button_3 = new JButton("Detalhar orçamento");
		button_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					if (table.getSelectedRow() >= 0){
						int idOrc = (int) table.getValueAt( table.getSelectedRow(), 0);
						Orcamento orc = Fachada.localizarOrcamento(idOrc);

						if(orc != null) {
							String texto="";
							if(orc.getComponentes().isEmpty())
								texto = "Orçamento não possui nenhum componente. ";
							else
								texto += ("  ORÇ: " + "0" + orc.getId() + "  - CLIENTE: " + orc.getCliente().getNome() + "        \n\n");  
								for (Componente comp : orc.getComponentes()) {
									texto += "ID: " + comp.getId() + "    Peça: " + comp.getDescricao() + 
											" Preço: " + comp.getPreco() + "         \n";
								}
							
							texto += ("\n\nTotal: " + orc.getValor());

							JOptionPane.showMessageDialog(frame, texto, "Componentes", 1);
						}
					}
				}
				catch(Exception erro) {
					label.setText(erro.getMessage());
				}
			}
		});
		button_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		button_3.setBounds(360, 200, 150, 23);
		frame.getContentPane().add(button_3);
		
		
	}
	

	public void listagem() {
		try{
			//ler os carros do banco
			List<Orcamento> lista = Fachada.listarOrcamentos();

			// o model armazena todas as linhas e colunas do table
			DefaultTableModel model = new DefaultTableModel();

			//adicionar colunas no model
			model.addColumn("id");
			model.addColumn("Data");
			model.addColumn("Cliente");
			model.addColumn("Valor Total");

			//adicionar linhas no model
			for(Orcamento orc : lista) {
				model.addRow(new Object[]{orc.getId(), orc.getData(), orc.getCliente().getNome(), orc.getValor()});
			}


			//atualizar model no table (visualizacao)
			table.setModel(model);

			label_3.setText("resultados: "+lista.size()+ " objetos");
		}
		catch(Exception erro){
			label.setText(erro.getMessage());
		}
	}

	


}
