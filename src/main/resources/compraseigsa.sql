-- MySQL Script generated by MySQL Workbench
-- Wed Jan  8 16:48:14 2025
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema compraseigsa
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema compraseigsa
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `compraseigsa` DEFAULT CHARACTER SET utf8mb4 ;
USE `compraseigsa` ;

-- -----------------------------------------------------
-- Table `Producto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Producto` (
  `id_producto` INT NOT NULL AUTO_INCREMENT,
  `descripcion` TEXT NOT NULL,
  PRIMARY KEY (`id_producto`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Proveedor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Proveedor` (
  `id_proveedor` INT NOT NULL AUTO_INCREMENT,
  `nombre` TEXT NOT NULL,
  `email` VARCHAR(60) NULL,
  `telefono` VARCHAR(30) NULL,
  `ubicacion` VARCHAR(250) NULL,
  PRIMARY KEY (`id_proveedor`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Producto_proveedor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Producto_proveedor` (
  `idproducto_proveedor` INT NOT NULL AUTO_INCREMENT,
  `precio_ofrecido` DECIMAL(10,2) NULL,
  `disponibilidad` ENUM('disponible', 'no_disponible') NULL,
  `id_producto` INT NOT NULL,
  `id_proveedor` INT NOT NULL,
  PRIMARY KEY (`idproducto_proveedor`),
  INDEX `id_producto_idx` (`id_producto` ASC) VISIBLE,
  INDEX `id_proveedor_idx` (`id_proveedor` ASC) VISIBLE,
  CONSTRAINT `id_producto`
    FOREIGN KEY (`id_producto`)
    REFERENCES `Producto` (`id_producto`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `id_proveedor`
    FOREIGN KEY (`id_proveedor`)
    REFERENCES `Proveedor` (`id_proveedor`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Usuario` (
  `id_usuario` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(60) NOT NULL,
  `email` VARCHAR(60) NULL,
  `tipo` ENUM('administrador', 'empleado') NOT NULL,
  `contrasena` VARCHAR(255) NOT NULL,
  `estatus` ENUM('activo', "inactivo") NULL DEFAULT 'activo',
  PRIMARY KEY (`id_usuario`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Auditoria`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Auditoria` (
  `id_Auditoria` INT NOT NULL AUTO_INCREMENT,
  `tabla_afectada` VARCHAR(50) NULL,
  `id_registro_afectado` INT NULL,
  `accion` ENUM('insertar', 'actualizar', 'eliminar') NULL,
  `fecha_cambio` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `descripcion_cambio` TEXT NULL,
  `id_usuario` INT NOT NULL,
  PRIMARY KEY (`id_Auditoria`),
  INDEX `id_usuario_idx` (`id_usuario` ASC) VISIBLE,
  CONSTRAINT `fk_auditoria_usuario`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `Usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Compra`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Compra` (
  `id_compra` INT NOT NULL AUTO_INCREMENT,
  `orden_compra` VARCHAR(40) NOT NULL,
  `condiciones` VARCHAR(60) NULL,
  `fecha_emision` DATE NULL,
  `orden_trabajo` VARCHAR(40) NULL,
  `fecha_entrega` DATE NULL,
  `agente_proveedor` TEXT NULL,
  `nombre_comprador` VARCHAR(60) NULL,
  `revisado_por` VARCHAR(60) NULL,
  `aprobado_por` VARCHAR(60) NULL,
  `estatus` ENUM('pendiente', 'entregado', 'cancelado') NULL,
  `notas_generales` TEXT NULL,
  `tipo` ENUM('compra', 'requisicion', 'renta') NULL,
  `fecha_inicio_renta` DATE NULL,
  `fecha_fin_renta` DATE NULL,
  `id_proveedor` INT NOT NULL,
  `id_usuario` INT NOT NULL,
  PRIMARY KEY (`id_compra`),
  INDEX `id_proveedor_idx` (`id_proveedor` ASC) VISIBLE,
  INDEX `id_usuario_idx` (`id_usuario` ASC) VISIBLE,
  CONSTRAINT `id_proveedor_compra`
    FOREIGN KEY (`id_proveedor`)
    REFERENCES `Proveedor` (`id_proveedor`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `id_usuario_compra`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `Usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Compra_producto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Compra_producto` (
  `idcompra_producto` INT NOT NULL AUTO_INCREMENT,
  `id_compra` INT NOT NULL,
  `id_producto` INT NOT NULL,
  `partida` INT NULL,
  `cantidad` VARCHAR(10) NULL,
  `precio_unitario` DECIMAL(10,2) NULL,
  `total` DECIMAL(10,2) NULL,
  PRIMARY KEY (`idcompra_producto`),
  INDEX `id_compra_idx` (`id_compra` ASC) VISIBLE,
  INDEX `id_producto_idx` (`id_producto` ASC) VISIBLE,
  CONSTRAINT `id_compra_comprod`
    FOREIGN KEY (`id_compra`)
    REFERENCES `Compra` (`id_compra`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `id_producto_comprod`
    FOREIGN KEY (`id_producto`)
    REFERENCES `Producto` (`id_producto`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Notificacion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Notificacion` (
  `id_notificacion` INT NOT NULL AUTO_INCREMENT,
  `fecha` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `mensaje` TEXT NULL,
  `id_compra` INT NOT NULL,
  PRIMARY KEY (`id_notificacion`),
  INDEX `id_compra_idx` (`id_compra` ASC) VISIBLE,
  CONSTRAINT `id_compra`
    FOREIGN KEY (`id_compra`)
    REFERENCES `Compra` (`id_compra`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `recuperacionPassword`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `recuperacionPassword` (
  `idRecuperacion` INT NOT NULL AUTO_INCREMENT,
  `id_usuario` INT NOT NULL,
  `codigo_recuperacion` VARCHAR(6) NOT NULL,
  `fecha_expiracion` TIMESTAMP NOT NULL,
  PRIMARY KEY (`idRecuperacion`),
  INDEX `id_usuario_idx` (`id_usuario` ASC) VISIBLE,
  CONSTRAINT `fk_recuperacion_usuario`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `Usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
