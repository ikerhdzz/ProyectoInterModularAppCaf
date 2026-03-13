export interface ElementoMenu {
  nombre: string;
  precio: number;
  id: number;
  imagen?: string;
  categoriaId?: number;
  categoriaNombre?: string;
}

export interface ElementoPedido extends ElementoMenu {
  cantidad: number;
}

export interface ItemPedido {
  id: number;
  nombre: string;
  cantidad: number;
  precio: number;
}

export interface Pedido {
  id: number;
  usuario?: {
    id: number;
    nombre: string;
    email: string;
  };
  items?: ItemPedido[];
  estado?: string;
  total?: number;
  fecha?: string;
}

export interface Usuario {
  id: number;
  nombre: string;
  email: string;
  rol: {
    id: number;
    nombre: string;
  };
  centro?: {
    id: number;
    nombre: string;
  };
}

export type Pantalla = "login" | "registro" | "menu" | "pago" | "stock" | "admin" | "pedidosAdmin" | "empleados" | "estadisticas" | "cocina";
