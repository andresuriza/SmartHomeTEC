﻿// <auto-generated />
using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Migrations;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;
using Proyecto1.Models;

#nullable disable

namespace Proyecto1.Migrations
{
    [DbContext(typeof(SmartHomeDbContext))]
    [Migration("20241017101822_20")]
    partial class _20
    {
        /// <inheritdoc />
        protected override void BuildTargetModel(ModelBuilder modelBuilder)
        {
#pragma warning disable 612, 618
            modelBuilder
                .HasAnnotation("ProductVersion", "8.0.10")
                .HasAnnotation("Relational:MaxIdentifierLength", 63);

            NpgsqlModelBuilderExtensions.UseIdentityByDefaultColumns(modelBuilder);

            modelBuilder.Entity("DireccionEntrega", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("integer");

                    NpgsqlPropertyBuilderExtensions.UseIdentityByDefaultColumn(b.Property<int>("Id"));

                    b.Property<string>("Calle")
                        .IsRequired()
                        .HasColumnType("text");

                    b.Property<string>("Ciudad")
                        .IsRequired()
                        .HasColumnType("text");

                    b.Property<string>("CodigoPostal")
                        .IsRequired()
                        .HasColumnType("text");

                    b.Property<string>("Pais")
                        .IsRequired()
                        .HasColumnType("text");

                    b.Property<int?>("UserId")
                        .HasColumnType("integer");

                    b.HasKey("Id");

                    b.HasIndex("UserId");

                    b.ToTable("DireccionesEntrega");
                });

            modelBuilder.Entity("Dispositivo", b =>
                {
                    b.Property<string>("NumeroSerie")
                        .HasColumnType("text");

                    b.Property<decimal>("ConsumoElectrico")
                        .HasColumnType("numeric");

                    b.Property<int?>("DistribuidorId")
                        .HasColumnType("integer");

                    b.Property<string>("Marca")
                        .IsRequired()
                        .HasColumnType("text");

                    b.Property<string>("Region")
                        .IsRequired()
                        .HasColumnType("text");

                    b.Property<int>("TipoDispositivoId")
                        .HasColumnType("integer");

                    b.HasKey("NumeroSerie");

                    b.HasIndex("DistribuidorId");

                    b.HasIndex("NumeroSerie")
                        .IsUnique();

                    b.HasIndex("TipoDispositivoId");

                    b.ToTable("Dispositivos");
                });

            modelBuilder.Entity("Proyecto1.Models.CertificadoGarantia", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("integer");

                    NpgsqlPropertyBuilderExtensions.UseIdentityByDefaultColumn(b.Property<int>("Id"));

                    b.Property<string>("DispositivoNumeroSerie")
                        .IsRequired()
                        .HasColumnType("text");

                    b.Property<DateTime>("FechaCompra")
                        .HasColumnType("timestamp with time zone");

                    b.Property<DateTime>("FechaFinGarantia")
                        .HasColumnType("timestamp with time zone");

                    b.Property<int>("UsuarioId")
                        .HasColumnType("integer");

                    b.HasKey("Id");

                    b.HasIndex("DispositivoNumeroSerie");

                    b.HasIndex("UsuarioId");

                    b.ToTable("CertificadosGarantia");
                });

            modelBuilder.Entity("Proyecto1.Models.DispositivoUsuario", b =>
                {
                    b.Property<int>("UserId")
                        .HasColumnType("integer");

                    b.Property<string>("DispositivoNumeroSerie")
                        .HasColumnType("text");

                    b.Property<string>("Aposento")
                        .IsRequired()
                        .HasColumnType("text");

                    b.Property<DateTime>("FechaAsociacion")
                        .HasColumnType("timestamp with time zone");

                    b.Property<int>("GarantiaRestante")
                        .HasColumnType("integer");

                    b.Property<int>("Id")
                        .HasColumnType("integer");

                    b.HasKey("UserId", "DispositivoNumeroSerie");

                    b.HasIndex("DispositivoNumeroSerie");

                    b.ToTable("DispositivosUsuarios");
                });

            modelBuilder.Entity("Proyecto1.Models.Distribuidor", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("integer");

                    NpgsqlPropertyBuilderExtensions.UseIdentityByDefaultColumn(b.Property<int>("Id"));

                    b.Property<string>("CedulaJuridica")
                        .IsRequired()
                        .HasColumnType("text");

                    b.Property<string>("Nombre")
                        .IsRequired()
                        .HasColumnType("text");

                    b.Property<string>("Region")
                        .IsRequired()
                        .HasColumnType("text");

                    b.HasKey("Id");

                    b.ToTable("Distribuidores");
                });

            modelBuilder.Entity("Proyecto1.Models.Factura", b =>
                {
                    b.Property<int>("NumeroFactura")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("integer");

                    NpgsqlPropertyBuilderExtensions.UseIdentityByDefaultColumn(b.Property<int>("NumeroFactura"));

                    b.Property<string>("DispositivoNumeroSerie")
                        .IsRequired()
                        .HasColumnType("text");

                    b.Property<DateTime>("FechaCompra")
                        .HasColumnType("timestamp with time zone");

                    b.Property<decimal>("Precio")
                        .HasColumnType("numeric");

                    b.Property<int>("UsuarioId")
                        .HasColumnType("integer");

                    b.HasKey("NumeroFactura");

                    b.HasIndex("DispositivoNumeroSerie");

                    b.HasIndex("UsuarioId");

                    b.ToTable("Facturas");
                });

            modelBuilder.Entity("Proyecto1.Models.HistorialUsuariosDispositivos", b =>
                {
                    b.Property<int>("UsuarioId")
                        .HasColumnType("integer");

                    b.Property<string>("DispositivoNumeroSerie")
                        .HasColumnType("text");

                    b.Property<DateTime>("FechaTransferencia")
                        .HasColumnType("timestamp with time zone");

                    b.HasKey("UsuarioId", "DispositivoNumeroSerie", "FechaTransferencia");

                    b.HasIndex("DispositivoNumeroSerie");

                    b.ToTable("HistorialUsuariosDispositivos");
                });

            modelBuilder.Entity("Proyecto1.Models.Pedido", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("integer");

                    NpgsqlPropertyBuilderExtensions.UseIdentityByDefaultColumn(b.Property<int>("Id"));

                    b.Property<string>("DispositivoNumeroSerie")
                        .IsRequired()
                        .HasColumnType("text");

                    b.Property<DateTime>("FechaHora")
                        .HasColumnType("timestamp with time zone");

                    b.Property<decimal>("MontoTotal")
                        .HasColumnType("numeric");

                    b.Property<int>("NumeroPedido")
                        .HasColumnType("integer");

                    b.Property<int>("UsuarioId")
                        .HasColumnType("integer");

                    b.HasKey("Id");

                    b.HasIndex("DispositivoNumeroSerie");

                    b.HasIndex("UsuarioId");

                    b.ToTable("Pedidos");
                });

            modelBuilder.Entity("TipoDispositivo", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("integer");

                    NpgsqlPropertyBuilderExtensions.UseIdentityByDefaultColumn(b.Property<int>("Id"));

                    b.Property<string>("Descripcion")
                        .IsRequired()
                        .HasColumnType("text");

                    b.Property<string>("Nombre")
                        .IsRequired()
                        .HasColumnType("text");

                    b.Property<int>("TiempoGarantia")
                        .HasColumnType("integer");

                    b.HasKey("Id");

                    b.ToTable("TiposDispositivos");
                });

            modelBuilder.Entity("User", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("integer");

                    NpgsqlPropertyBuilderExtensions.UseIdentityByDefaultColumn(b.Property<int>("Id"));

                    b.Property<string>("Apellidos")
                        .IsRequired()
                        .HasColumnType("text");

                    b.Property<string>("Contrasena")
                        .IsRequired()
                        .HasColumnType("text");

                    b.Property<string>("CorreoElectronico")
                        .IsRequired()
                        .HasColumnType("text");

                    b.Property<string>("Nombre")
                        .IsRequired()
                        .HasColumnType("text");

                    b.Property<string>("Region")
                        .IsRequired()
                        .HasColumnType("text");

                    b.HasKey("Id");

                    b.HasIndex("CorreoElectronico")
                        .IsUnique();

                    b.ToTable("Users");
                });

            modelBuilder.Entity("DireccionEntrega", b =>
                {
                    b.HasOne("User", "User")
                        .WithMany("DireccionesEntrega")
                        .HasForeignKey("UserId");

                    b.Navigation("User");
                });

            modelBuilder.Entity("Dispositivo", b =>
                {
                    b.HasOne("Proyecto1.Models.Distribuidor", null)
                        .WithMany("Dispositivos")
                        .HasForeignKey("DistribuidorId");

                    b.HasOne("TipoDispositivo", "TipoDispositivo")
                        .WithMany("Dispositivos")
                        .HasForeignKey("TipoDispositivoId")
                        .OnDelete(DeleteBehavior.Restrict)
                        .IsRequired();

                    b.Navigation("TipoDispositivo");
                });

            modelBuilder.Entity("Proyecto1.Models.CertificadoGarantia", b =>
                {
                    b.HasOne("Dispositivo", "Dispositivo")
                        .WithMany()
                        .HasForeignKey("DispositivoNumeroSerie")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("User", "Usuario")
                        .WithMany()
                        .HasForeignKey("UsuarioId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("Dispositivo");

                    b.Navigation("Usuario");
                });

            modelBuilder.Entity("Proyecto1.Models.DispositivoUsuario", b =>
                {
                    b.HasOne("Dispositivo", "Dispositivo")
                        .WithMany("DispositivosUsuarios")
                        .HasForeignKey("DispositivoNumeroSerie")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("User", "User")
                        .WithMany("Dispositivos")
                        .HasForeignKey("UserId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("Dispositivo");

                    b.Navigation("User");
                });

            modelBuilder.Entity("Proyecto1.Models.Factura", b =>
                {
                    b.HasOne("Dispositivo", "Dispositivo")
                        .WithMany("Facturas")
                        .HasForeignKey("DispositivoNumeroSerie")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("User", "Usuario")
                        .WithMany("Facturas")
                        .HasForeignKey("UsuarioId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("Dispositivo");

                    b.Navigation("Usuario");
                });

            modelBuilder.Entity("Proyecto1.Models.HistorialUsuariosDispositivos", b =>
                {
                    b.HasOne("Dispositivo", "Dispositivo")
                        .WithMany()
                        .HasForeignKey("DispositivoNumeroSerie")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("User", "Usuario")
                        .WithMany()
                        .HasForeignKey("UsuarioId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("Dispositivo");

                    b.Navigation("Usuario");
                });

            modelBuilder.Entity("Proyecto1.Models.Pedido", b =>
                {
                    b.HasOne("Dispositivo", "Dispositivo")
                        .WithMany("Pedidos")
                        .HasForeignKey("DispositivoNumeroSerie")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("User", "Usuario")
                        .WithMany("Pedidos")
                        .HasForeignKey("UsuarioId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("Dispositivo");

                    b.Navigation("Usuario");
                });

            modelBuilder.Entity("Dispositivo", b =>
                {
                    b.Navigation("DispositivosUsuarios");

                    b.Navigation("Facturas");

                    b.Navigation("Pedidos");
                });

            modelBuilder.Entity("Proyecto1.Models.Distribuidor", b =>
                {
                    b.Navigation("Dispositivos");
                });

            modelBuilder.Entity("TipoDispositivo", b =>
                {
                    b.Navigation("Dispositivos");
                });

            modelBuilder.Entity("User", b =>
                {
                    b.Navigation("DireccionesEntrega");

                    b.Navigation("Dispositivos");

                    b.Navigation("Facturas");

                    b.Navigation("Pedidos");
                });
#pragma warning restore 612, 618
        }
    }
}
