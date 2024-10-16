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
    public class TipoDispositivoController : ControllerBase
    {
        private readonly SmartHomeDbContext _context;

        public TipoDispositivoController(SmartHomeDbContext context)
        {
            _context = context;
        }

        // GET: api/TipoDispositivo
        [HttpGet]
        public async Task<ActionResult<IEnumerable<TipoDispositivo>>> GetTiposDispositivos()
        {
            return await _context.TiposDispositivos.ToListAsync();
        }

        // GET: api/TipoDispositivo/5
        [HttpGet("{id}")]
        public async Task<ActionResult<TipoDispositivo>> GetTipoDispositivo(int id)
        {
            var tipoDispositivo = await _context.TiposDispositivos.FindAsync(id);

            if (tipoDispositivo == null)
            {
                return NotFound();
            }

            return tipoDispositivo;
        }

        // POST: api/TipoDispositivo
        [HttpPost]
        public async Task<ActionResult<TipoDispositivo>> PostTipoDispositivo(TipoDispositivo tipoDispositivo)
        {
            _context.TiposDispositivos.Add(tipoDispositivo);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetTipoDispositivo", new { id = tipoDispositivo.Id }, tipoDispositivo);
        }

        // PUT: api/TipoDispositivo/5
        [HttpPut("{id}")]
        public async Task<IActionResult> PutTipoDispositivo(int id, TipoDispositivo tipoDispositivo)
        {
            if (id != tipoDispositivo.Id)
            {
                return BadRequest();
            }

            _context.Entry(tipoDispositivo).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!TipoDispositivoExists(id))
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

        // DELETE: api/TipoDispositivo/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteTipoDispositivo(int id)
        {
            var tipoDispositivo = await _context.TiposDispositivos.FindAsync(id);
            if (tipoDispositivo == null)
            {
                return NotFound();
            }

            _context.TiposDispositivos.Remove(tipoDispositivo);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool TipoDispositivoExists(int id)
        {
            return _context.TiposDispositivos.Any(e => e.Id == id);
        }
    }
}
