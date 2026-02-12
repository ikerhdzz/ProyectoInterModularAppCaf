export interface ElementoMenu {
  id: number;
  nombre: string;
  precio: number;
  imagen?: string;
  categoriaId?: number;
}


export interface ElementoPedido extends ElementoMenu {
  cantidad: number;
}