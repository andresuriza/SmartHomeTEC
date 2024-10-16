namespace Proyecto1.Models
{
    public class CertificadoGarantia
    {
        public int Id { get; set; }
        public DateTime FechaCompra { get; set; }
        public DateTime FechaFinGarantia { get; set; }

        public int UsuarioId { get; set; }
        public User Usuario { get; set; }

        public int DispositivoId { get; set; }
        public Dispositivo Dispositivo { get; set; }
    }

}
