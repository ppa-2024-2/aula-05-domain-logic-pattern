package br.edu.ifrs.riogrande.tads.ppa.ligaa.entity;

import java.util.List;

import br.edu.ifrs.riogrande.tads.ppa.ligaa.entity.Matricula.Situacao;

// Value Object => Objeto de Valor 
// DTO => VO => DTO
public record Historico(Aluno aluno, List<Turma> turmas) {

    public boolean aprovadoEm(Disciplina disciplina) {
        // FIXME: considerar a disciplina
        return turmas.stream().flatMap(t -> t.getMatriculas().stream())
            .anyMatch(m -> m.getAluno().equals(aluno) && m.getSituacao().equals(Situacao.APROVADO));
    }

}
