package com.Sortlistview;

public abstract class SimpleSortHand implements SortHand{

	public abstract String getSortContent();
	
	@Override
	public String getSort() {
		// TODO Auto-generated method stub
		CharacterParser chp = getCharacter();
		if(getSortContent() == null || getSortContent().length() == 0){
			return "#";
		}
		String phead = chp.getSelling(getSortContent()).substring(0, 1).toUpperCase();
		if(phead.matches("[A-Z]")){
			return phead;
		}
		return "#";
	}

	@Override
	public abstract CharacterParser getCharacter() ;

}
