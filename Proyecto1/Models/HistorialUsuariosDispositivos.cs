namespace Proyecto1.Models
{
    public class HistorialUsuariosDispositivos
    {
        public int UsuarioId { get; set; }
        public User Usuario { get; set; }

        public int DispositivoId { get; set; }
        public Dispositivo Dispositivo { get; set; }

        public DateTime FechaTransferencia { get; set; }
    }

}
