package com.shangpin.ephub.client.common.dto;

/**
 * <p>Title:RowBoundsDto.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午3:31:31
 */
public class RowBoundsDto {
	  public static final int NO_ROW_OFFSET = 0;
	  public static final int NO_ROW_LIMIT = Integer.MAX_VALUE;
	  public static final RowBoundsDto DEFAULT = new RowBoundsDto();

	  private int offset;
	  private int limit;

	  public RowBoundsDto() {
	    this.offset = NO_ROW_OFFSET;
	    this.limit = NO_ROW_LIMIT;
	  }

	  public RowBoundsDto(int offset, int limit) {
	    this.offset = offset;
	    this.limit = limit;
	  }

	  public int getOffset() {
	    return offset;
	  }

	  public int getLimit() {
	    return limit;
	  }
}
