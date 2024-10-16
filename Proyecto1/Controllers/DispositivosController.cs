using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Proyecto1.Models;

namespace Proyecto1.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class DispositivosController : ControllerBase
    {
        private readonly SmartHomeDbContext _context;

        public DispositivosController(SmartHomeDbContext context)
        {
            _context = context;
        }

        // GET: api/Dispositivos
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Dispositivo>>> GetDispositivos()
        {
            return await _context.Dispositivos
                .Include(d => d.TipoDispositivo) // Incluye los datos del TipoDispositivo relacionado
                .ToListAsync();
        }

        // GET: api/Dispositivos/5
        [HttpGet("{id}")]
        public async Task<ActionResult<Dispositivo>> GetDispositivo(int id)
        {
            var dispositivo = await _context.Dispositivos
                .Include(d => d.TipoDispositivo) // Incluye los datos del TipoDispositivo relacionado
                .FirstOrDefaultAsync(d => d.Id == id);

            if (dispositivo == null)
            {
                return NotFound();
            }

            return dispositivo;
        }

        // POST: api/Dispositivos
        [HttpPost]
        public async Task<ActionResult<Dispositivo>> PostDispositivo(Dispositivo dispositivo)
        {
            // Validar si el TipoDispositivoId existe
            var tipoDispositivo = await _context.TiposDispositivos.FindAsync(dispositivo.TipoDispositivoId);
            if (tipoDispositivo == null)
            {
                return BadRequest(new { message = "El TipoDispositivo no existe." });
            }

            // Asignar el TipoDispositivo encontrado al dispositivo
            dispositivo.TipoDispositivo = tipoDispositivo;

            _context.Dispositivos.Add(dispositivo);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetDispositivo", new { id = dispositivo.Id }, dispositivo);
        }



        // PUT: api/Dispositivos/5
        [HttpPut("{id}")]
        public async Task<IActionResult> PutDispositivo(int id, Dispositivo dispositivo)
        {
            if (id != dispositivo.Id)
            {
                return BadRequest();
            }

            // Validar que el TipoDispositivoId exista en la base de datos
            var tipoDispositivo = await _context.TiposDispositivos.FindAsync(dispositivo.TipoDispositivoId);
            if (tipoDispositivo == null)
            {
                return BadRequest("El TipoDispositivoId proporcionado no es válido.");
            }

            _context.Entry(dispositivo).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!DispositivoExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return NoContent();
        }

        // DELETE: api/Dispositivos/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteDispositivo(int id)
        {
            var dispositivo = await _context.Dispositivos.FindAsync(id);
            if (dispositivo == null)
            {
                return NotFound();
            }

            _context.Dispositivos.Remove(dispositivo);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool DispositivoExists(int id)
        {
            return _context.Dispositivos.Any(e => e.Id == id);
        }
    }
}
