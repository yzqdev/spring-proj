namespace Yi.Framework.OcelotGateway.Builder
{
    public abstract class AbstractBuilder
    {

        public abstract void Invoke(DataContext data);

        private  AbstractBuilder? NextBuilder=null;


        public void SetNext(AbstractBuilder? nextBuilder)
        {
            this.NextBuilder = nextBuilder;
        }

        public void Next( DataContext data)
        {
            if (NextBuilder != null)
            {
                this.NextBuilder!.Invoke(data!);
            }

        }
    }
}
