class CountingSort {
  int[] sort(int[] nums, int k) {
    int[] workspace = new int[k + 1];
    int[] result = new int[nums.length];
    
    //count the number of elements including duplicates
    for(int i = 0; i < nums.length; i++) {
      workspace[nums[i]]++;
    }
    
    //count the overall numbers
    for(int i = 1; i < workspace.length; i++) {
      workspace[i] = workspace[i] + workspace[i - 1];
    }
    
    //copy the number from original array and decrement the count from workspace
    for(int i = nums.length - 1; i >= 0; i--) {
      result[workspace[nums[i]] - 1] = nums[i];
      workspace[nums[i]] --;
    }
    
    return result;
  }
}
