package br.edu.ifrs.riogrande.tads.ppa.ligaa.entity;

import java.util.ArrayList;
import java.util.List;

public class Turma {

    private String codigo; // ppa-2024-2

    private Disciplina disciplina;
    private Professor professor;

    private String semestre; // 2024-2
    private String sala;
    private int vagas;

    private List<Matricula> matriculas = new ArrayList<>();

    public void setMatriculas(List<Matricula> matriculas) {
        this.matriculas = matriculas;
    }

    public List<Matricula> getMatriculas() {
        return matriculas;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public int getVagas() {
        return vagas;
    }

    public void setVagas(int vagas) {
        this.vagas = vagas;
    }

    public boolean isFechada() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isFechada'");
    }

}
