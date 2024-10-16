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
    public class HistorialUsuariosDispositivosController : ControllerBase
    {
        private readonly SmartHomeDbContext _context;

        public HistorialUsuariosDispositivosController(SmartHomeDbContext context)
        {
            _context = context;
        }

        // GET: api/HistorialUsuariosDispositivos
        [HttpGet]
        public async Task<ActionResult<IEnumerable<HistorialUsuariosDispositivos>>> GetHistoriales()
        {
            return await _context.HistorialUsuariosDispositivos.ToListAsync();
        }

        // GET: api/HistorialUsuariosDispositivos/5
        [HttpGet("{UsuarioId}/{DispositivoId}")]
        public async Task<ActionResult<HistorialUsuariosDispositivos>> GetHistorial(int UsuarioId, int DispositivoId)
        {
            var historial = await _context.HistorialUsuariosDispositivos
                .FirstOrDefaultAsync(h => h.UsuarioId == UsuarioId && h.DispositivoId == DispositivoId);

            if (historial == null)
            {
                return NotFound();
            }

            return historial;
        }

        // POST: api/HistorialUsuariosDispositivos
        [HttpPost]
        public async Task<ActionResult<HistorialUsuariosDispositivos>> PostHistorial(HistorialUsuariosDispositivos historial)
        {
            _context.HistorialUsuariosDispositivos.Add(historial);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetHistorial", new { UsuarioId = historial.UsuarioId, DispositivoId = historial.DispositivoId }, historial);
        }

        // PUT: api/HistorialUsuariosDispositivos/5
        [HttpPut("{UsuarioId}/{DispositivoId}")]
        public async Task<IActionResult> PutHistorial(int UsuarioId, int DispositivoId, HistorialUsuariosDispositivos historial)
        {
            if (UsuarioId != historial.UsuarioId || DispositivoId != historial.DispositivoId)
            {
                return BadRequest();
            }

            _context.Entry(historial).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!HistorialExists(UsuarioId, DispositivoId))
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

        // DELETE: api/HistorialUsuariosDispositivos/5
        [HttpDelete("{UsuarioId}/{DispositivoId}")]
        public async Task<IActionResult> DeleteHistorial(int UsuarioId, int DispositivoId)
        {
            var historial = await _context.HistorialUsuariosDispositivos
                .FirstOrDefaultAsync(h => h.UsuarioId == UsuarioId && h.DispositivoId == DispositivoId);

            if (historial == null)
            {
                return NotFound();
            }

            _context.HistorialUsuariosDispositivos.Remove(historial);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool HistorialExists(int UsuarioId, int DispositivoId)
        {
            return _context.HistorialUsuariosDispositivos.Any(h => h.UsuarioId == UsuarioId && h.DispositivoId == DispositivoId);
        }
    }
}
