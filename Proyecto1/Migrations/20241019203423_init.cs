using System;
using Microsoft.EntityFrameworkCore.Migrations;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;

#nullable disable

namespace Proyecto1.Migrations
{
    /// <inheritdoc />
    public partial class init : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "Distribuidores",
                columns: table => new
                {
                    CedulaJuridica = table.Column<string>(type: "text", nullable: false),
                    Nombre = table.Column<string>(type: "text", nullable: false),
                    Region = table.Column<string>(type: "text", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Distribuidores", x => x.CedulaJuridica);
                });

            migrationBuilder.CreateTable(
                name: "TiposDispositivos",
                columns: table => new
                {
                    Id = table.Column<int>(type: "integer", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    Nombre = table.Column<string>(type: "text", nullable: false),
                    Descripcion = table.Column<string>(type: "text", nullable: false),
                    TiempoGarantia = table.Column<int>(type: "integer", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_TiposDispositivos", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "Users",
                columns: table => new
                {
                    Id = table.Column<int>(type: "integer", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    Nombre = table.Column<string>(type: "text", nullable: false),
                    Apellidos = table.Column<string>(type: "text", nullable: false),
                    Region = table.Column<string>(type: "text", nullable: false),
                    CorreoElectronico = table.Column<string>(type: "text", nullable: false),
                    Contrasena = table.Column<string>(type: "text", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Users", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "Dispositivos",
                columns: table => new
                {
                    NumeroSerie = table.Column<string>(type: "text", nullable: false),
                    Marca = table.Column<string>(type: "text", nullable: false),
                    ConsumoElectrico = table.Column<decimal>(type: "numeric", nullable: false),
                    TipoDispositivoId = table.Column<int>(type: "integer", nullable: false),
                    DistribuidorCedulaJuridica = table.Column<string>(type: "text", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Dispositivos", x => x.NumeroSerie);
                    table.ForeignKey(
                        name: "FK_Dispositivos_Distribuidores_DistribuidorCedulaJuridica",
                        column: x => x.DistribuidorCedulaJuridica,
                        principalTable: "Distribuidores",
                        principalColumn: "CedulaJuridica");
                    table.ForeignKey(
                        name: "FK_Dispositivos_TiposDispositivos_TipoDispositivoId",
                        column: x => x.TipoDispositivoId,
                        principalTable: "TiposDispositivos",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Restrict);
                });

            migrationBuilder.CreateTable(
                name: "DireccionesEntrega",
                columns: table => new
                {
                    Id = table.Column<int>(type: "integer", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    Calle = table.Column<string>(type: "text", nullable: false),
                    Ciudad = table.Column<string>(type: "text", nullable: false),
                    CodigoPostal = table.Column<string>(type: "text", nullable: false),
                    Pais = table.Column<string>(type: "text", nullable: false),
                    UserId = table.Column<int>(type: "integer", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_DireccionesEntrega", x => x.Id);
                    table.ForeignKey(
                        name: "FK_DireccionesEntrega_Users_UserId",
                        column: x => x.UserId,
                        principalTable: "Users",
                        principalColumn: "Id");
                });

            migrationBuilder.CreateTable(
                name: "CertificadosGarantia",
                columns: table => new
                {
                    Id = table.Column<int>(type: "integer", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    FechaCompra = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
                    FechaFinGarantia = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
                    UsuarioId = table.Column<int>(type: "integer", nullable: false),
                    DispositivoNumeroSerie = table.Column<string>(type: "text", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_CertificadosGarantia", x => x.Id);
                    table.ForeignKey(
                        name: "FK_CertificadosGarantia_Dispositivos_DispositivoNumeroSerie",
                        column: x => x.DispositivoNumeroSerie,
                        principalTable: "Dispositivos",
                        principalColumn: "NumeroSerie",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_CertificadosGarantia_Users_UsuarioId",
                        column: x => x.UsuarioId,
                        principalTable: "Users",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "DispositivosUsuarios",
                columns: table => new
                {
                    UserId = table.Column<int>(type: "integer", nullable: false),
                    DispositivoNumeroSerie = table.Column<string>(type: "text", nullable: false),
                    Id = table.Column<int>(type: "integer", nullable: false),
                    FechaAsociacion = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
                    Aposento = table.Column<string>(type: "text", nullable: false),
                    GarantiaRestante = table.Column<int>(type: "integer", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_DispositivosUsuarios", x => new { x.UserId, x.DispositivoNumeroSerie });
                    table.ForeignKey(
                        name: "FK_DispositivosUsuarios_Dispositivos_DispositivoNumeroSerie",
                        column: x => x.DispositivoNumeroSerie,
                        principalTable: "Dispositivos",
                        principalColumn: "NumeroSerie",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_DispositivosUsuarios_Users_UserId",
                        column: x => x.UserId,
                        principalTable: "Users",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "Facturas",
                columns: table => new
                {
                    NumeroFactura = table.Column<int>(type: "integer", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    FechaCompra = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
                    UsuarioId = table.Column<int>(type: "integer", nullable: false),
                    DispositivoNumeroSerie = table.Column<string>(type: "text", nullable: false),
                    Precio = table.Column<decimal>(type: "numeric", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Facturas", x => x.NumeroFactura);
                    table.ForeignKey(
                        name: "FK_Facturas_Dispositivos_DispositivoNumeroSerie",
                        column: x => x.DispositivoNumeroSerie,
                        principalTable: "Dispositivos",
                        principalColumn: "NumeroSerie",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_Facturas_Users_UsuarioId",
                        column: x => x.UsuarioId,
                        principalTable: "Users",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "HistorialUsuariosDispositivos",
                columns: table => new
                {
                    UsuarioId = table.Column<int>(type: "integer", nullable: false),
                    DispositivoNumeroSerie = table.Column<string>(type: "text", nullable: false),
                    FechaTransferencia = table.Column<DateTime>(type: "timestamp with time zone", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_HistorialUsuariosDispositivos", x => new { x.UsuarioId, x.DispositivoNumeroSerie, x.FechaTransferencia });
                    table.ForeignKey(
                        name: "FK_HistorialUsuariosDispositivos_Dispositivos_DispositivoNumer~",
                        column: x => x.DispositivoNumeroSerie,
                        principalTable: "Dispositivos",
                        principalColumn: "NumeroSerie",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_HistorialUsuariosDispositivos_Users_UsuarioId",
                        column: x => x.UsuarioId,
                        principalTable: "Users",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "Pedidos",
                columns: table => new
                {
                    Id = table.Column<int>(type: "integer", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    UsuarioId = table.Column<int>(type: "integer", nullable: false),
                    DispositivoNumeroSerie = table.Column<string>(type: "text", nullable: false),
                    NumeroPedido = table.Column<int>(type: "integer", nullable: false),
                    FechaHora = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
                    MontoTotal = table.Column<decimal>(type: "numeric", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Pedidos", x => x.Id);
                    table.ForeignKey(
                        name: "FK_Pedidos_Dispositivos_DispositivoNumeroSerie",
                        column: x => x.DispositivoNumeroSerie,
                        principalTable: "Dispositivos",
                        principalColumn: "NumeroSerie",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_Pedidos_Users_UsuarioId",
                        column: x => x.UsuarioId,
                        principalTable: "Users",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "Productos",
                columns: table => new
                {
                    Id = table.Column<int>(type: "integer", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    Nombre = table.Column<string>(type: "text", nullable: false),
                    NumeroSerieDispositivo = table.Column<string>(type: "text", nullable: false),
                    DistribuidorCedula = table.Column<string>(type: "text", nullable: false),
                    Precio = table.Column<decimal>(type: "numeric", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Productos", x => x.Id);
                    table.ForeignKey(
                        name: "FK_Productos_Dispositivos_NumeroSerieDispositivo",
                        column: x => x.NumeroSerieDispositivo,
                        principalTable: "Dispositivos",
                        principalColumn: "NumeroSerie",
                        onDelete: ReferentialAction.SetNull);
                    table.ForeignKey(
                        name: "FK_Productos_Distribuidores_DistribuidorCedula",
                        column: x => x.DistribuidorCedula,
                        principalTable: "Distribuidores",
                        principalColumn: "CedulaJuridica",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                name: "IX_CertificadosGarantia_DispositivoNumeroSerie",
                table: "CertificadosGarantia",
                column: "DispositivoNumeroSerie");

            migrationBuilder.CreateIndex(
                name: "IX_CertificadosGarantia_UsuarioId",
                table: "CertificadosGarantia",
                column: "UsuarioId");

            migrationBuilder.CreateIndex(
                name: "IX_DireccionesEntrega_UserId",
                table: "DireccionesEntrega",
                column: "UserId");

            migrationBuilder.CreateIndex(
                name: "IX_Dispositivos_DistribuidorCedulaJuridica",
                table: "Dispositivos",
                column: "DistribuidorCedulaJuridica");

            migrationBuilder.CreateIndex(
                name: "IX_Dispositivos_NumeroSerie",
                table: "Dispositivos",
                column: "NumeroSerie",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_Dispositivos_TipoDispositivoId",
                table: "Dispositivos",
                column: "TipoDispositivoId");

            migrationBuilder.CreateIndex(
                name: "IX_DispositivosUsuarios_DispositivoNumeroSerie",
                table: "DispositivosUsuarios",
                column: "DispositivoNumeroSerie");

            migrationBuilder.CreateIndex(
                name: "IX_Facturas_DispositivoNumeroSerie",
                table: "Facturas",
                column: "DispositivoNumeroSerie");

            migrationBuilder.CreateIndex(
                name: "IX_Facturas_UsuarioId",
                table: "Facturas",
                column: "UsuarioId");

            migrationBuilder.CreateIndex(
                name: "IX_HistorialUsuariosDispositivos_DispositivoNumeroSerie",
                table: "HistorialUsuariosDispositivos",
                column: "DispositivoNumeroSerie");

            migrationBuilder.CreateIndex(
                name: "IX_Pedidos_DispositivoNumeroSerie",
                table: "Pedidos",
                column: "DispositivoNumeroSerie");

            migrationBuilder.CreateIndex(
                name: "IX_Pedidos_UsuarioId",
                table: "Pedidos",
                column: "UsuarioId");

            migrationBuilder.CreateIndex(
                name: "IX_Productos_DistribuidorCedula",
                table: "Productos",
                column: "DistribuidorCedula");

            migrationBuilder.CreateIndex(
                name: "IX_Productos_NumeroSerieDispositivo",
                table: "Productos",
                column: "NumeroSerieDispositivo",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_Users_CorreoElectronico",
                table: "Users",
                column: "CorreoElectronico",
                unique: true);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "CertificadosGarantia");

            migrationBuilder.DropTable(
                name: "DireccionesEntrega");

            migrationBuilder.DropTable(
                name: "DispositivosUsuarios");

            migrationBuilder.DropTable(
                name: "Facturas");

            migrationBuilder.DropTable(
                name: "HistorialUsuariosDispositivos");

            migrationBuilder.DropTable(
                name: "Pedidos");

            migrationBuilder.DropTable(
                name: "Productos");

            migrationBuilder.DropTable(
                name: "Users");

            migrationBuilder.DropTable(
                name: "Dispositivos");

            migrationBuilder.DropTable(
                name: "Distribuidores");

            migrationBuilder.DropTable(
                name: "TiposDispositivos");
        }
    }
}
