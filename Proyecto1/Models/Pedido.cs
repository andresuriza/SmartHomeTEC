namespace Proyecto1.Models
{
    public class Pedido
    {
        public int Id { get; set; }
        public int UsuarioId { get; set; }
        public User Usuario { get; set; }

        public int DispositivoId { get; set; }
        public Dispositivo Dispositivo { get; set; }

        public int NumeroPedido { get; set; }
        public DateTime FechaHora { get; set; }
        public decimal MontoTotal { get; set; }
    }

}
