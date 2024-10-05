export interface CommentaireDto {
  id?: number;
  contenu: string;
  dateCreation: string;
  utilisateurName: string;
  annonceId: number;
  utilisateurId: number;
}
